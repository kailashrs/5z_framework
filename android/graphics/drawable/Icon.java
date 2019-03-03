package android.graphics.drawable;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Executor;

public final class Icon
  implements Parcelable
{
  public static final Parcelable.Creator<Icon> CREATOR = new Parcelable.Creator()
  {
    public Icon createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Icon(paramAnonymousParcel, null);
    }
    
    public Icon[] newArray(int paramAnonymousInt)
    {
      return new Icon[paramAnonymousInt];
    }
  };
  static final PorterDuff.Mode DEFAULT_TINT_MODE = Drawable.DEFAULT_TINT_MODE;
  public static final int MIN_ASHMEM_ICON_SIZE = 131072;
  private static final String TAG = "Icon";
  public static final int TYPE_ADAPTIVE_BITMAP = 5;
  public static final int TYPE_BITMAP = 1;
  public static final int TYPE_DATA = 3;
  public static final int TYPE_RESOURCE = 2;
  public static final int TYPE_URI = 4;
  private static final int VERSION_STREAM_SERIALIZER = 1;
  private int mInt1;
  private int mInt2;
  private Object mObj1;
  private String mString1;
  private ColorStateList mTintList;
  private PorterDuff.Mode mTintMode = DEFAULT_TINT_MODE;
  private final int mType;
  
  private Icon(int paramInt)
  {
    mType = paramInt;
  }
  
  private Icon(Parcel paramParcel)
  {
    this(paramParcel.readInt());
    int i;
    Object localObject;
    switch (mType)
    {
    default: 
      paramParcel = new StringBuilder();
      paramParcel.append("invalid ");
      paramParcel.append(getClass().getSimpleName());
      paramParcel.append(" type in parcel: ");
      paramParcel.append(mType);
      throw new RuntimeException(paramParcel.toString());
    case 4: 
      mString1 = paramParcel.readString();
      break;
    case 3: 
      i = paramParcel.readInt();
      localObject = paramParcel.readBlob();
      if (i == localObject.length)
      {
        mInt1 = i;
        mObj1 = localObject;
      }
      else
      {
        paramParcel = new StringBuilder();
        paramParcel.append("internal unparceling error: blob length (");
        paramParcel.append(localObject.length);
        paramParcel.append(") != expected length (");
        paramParcel.append(i);
        paramParcel.append(")");
        throw new RuntimeException(paramParcel.toString());
      }
      break;
    case 2: 
      localObject = paramParcel.readString();
      i = paramParcel.readInt();
      mString1 = ((String)localObject);
      mInt1 = i;
      break;
    case 1: 
    case 5: 
      mObj1 = ((Bitmap)Bitmap.CREATOR.createFromParcel(paramParcel));
    }
    if (paramParcel.readInt() == 1) {
      mTintList = ((ColorStateList)ColorStateList.CREATOR.createFromParcel(paramParcel));
    }
  }
  
  public static Icon createFromStream(InputStream paramInputStream)
    throws IOException
  {
    DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
    if (localDataInputStream.readInt() >= 1) {
      switch (localDataInputStream.readByte())
      {
      default: 
        break;
      case 5: 
        return createWithAdaptiveBitmap(BitmapFactory.decodeStream(localDataInputStream));
      case 4: 
        return createWithContentUri(localDataInputStream.readUTF());
      case 3: 
        int i = localDataInputStream.readInt();
        paramInputStream = new byte[i];
        localDataInputStream.read(paramInputStream, 0, i);
        return createWithData(paramInputStream, 0, i);
      case 2: 
        return createWithResource(localDataInputStream.readUTF(), localDataInputStream.readInt());
      case 1: 
        return createWithBitmap(BitmapFactory.decodeStream(localDataInputStream));
      }
    }
    return null;
  }
  
  public static Icon createWithAdaptiveBitmap(Bitmap paramBitmap)
  {
    if (paramBitmap != null)
    {
      Icon localIcon = new Icon(5);
      localIcon.setBitmap(paramBitmap);
      return localIcon;
    }
    throw new IllegalArgumentException("Bitmap must not be null.");
  }
  
  public static Icon createWithBitmap(Bitmap paramBitmap)
  {
    if (paramBitmap != null)
    {
      Icon localIcon = new Icon(1);
      localIcon.setBitmap(paramBitmap);
      return localIcon;
    }
    throw new IllegalArgumentException("Bitmap must not be null.");
  }
  
  public static Icon createWithContentUri(Uri paramUri)
  {
    if (paramUri != null)
    {
      Icon localIcon = new Icon(4);
      mString1 = paramUri.toString();
      return localIcon;
    }
    throw new IllegalArgumentException("Uri must not be null.");
  }
  
  public static Icon createWithContentUri(String paramString)
  {
    if (paramString != null)
    {
      Icon localIcon = new Icon(4);
      mString1 = paramString;
      return localIcon;
    }
    throw new IllegalArgumentException("Uri must not be null.");
  }
  
  public static Icon createWithData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramArrayOfByte != null)
    {
      Icon localIcon = new Icon(3);
      mObj1 = paramArrayOfByte;
      mInt1 = paramInt2;
      mInt2 = paramInt1;
      return localIcon;
    }
    throw new IllegalArgumentException("Data must not be null.");
  }
  
  public static Icon createWithFilePath(String paramString)
  {
    if (paramString != null)
    {
      Icon localIcon = new Icon(4);
      mString1 = paramString;
      return localIcon;
    }
    throw new IllegalArgumentException("Path must not be null.");
  }
  
  public static Icon createWithResource(Context paramContext, int paramInt)
  {
    if (paramContext != null)
    {
      Icon localIcon = new Icon(2);
      mInt1 = paramInt;
      mString1 = paramContext.getPackageName();
      return localIcon;
    }
    throw new IllegalArgumentException("Context must not be null.");
  }
  
  public static Icon createWithResource(Resources paramResources, int paramInt)
  {
    if (paramResources != null)
    {
      Icon localIcon = new Icon(2);
      mInt1 = paramInt;
      mString1 = paramResources.getResourcePackageName(paramInt);
      return localIcon;
    }
    throw new IllegalArgumentException("Resource must not be null.");
  }
  
  public static Icon createWithResource(String paramString, int paramInt)
  {
    if (paramString != null)
    {
      Icon localIcon = new Icon(2);
      mInt1 = paramInt;
      mString1 = paramString;
      return localIcon;
    }
    throw new IllegalArgumentException("Resource package name must not be null.");
  }
  
  private Drawable loadDrawableInner(Context paramContext)
  {
    Object localObject1;
    Object localObject3;
    switch (mType)
    {
    default: 
      break;
    case 5: 
      return new AdaptiveIconDrawable(null, new BitmapDrawable(paramContext.getResources(), getBitmap()));
    case 4: 
      localObject1 = getUri();
      Object localObject2 = ((Uri)localObject1).getScheme();
      localObject3 = null;
      FileInputStream localFileInputStream = null;
      if ((!"content".equals(localObject2)) && (!"file".equals(localObject2))) {
        try
        {
          localFileInputStream = new java/io/FileInputStream;
          localObject2 = new java/io/File;
          ((File)localObject2).<init>(mString1);
          localFileInputStream.<init>((File)localObject2);
          localObject3 = localFileInputStream;
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Unable to load image from path: ");
          ((StringBuilder)localObject2).append(localObject1);
          Log.w("Icon", ((StringBuilder)localObject2).toString(), localFileNotFoundException);
        }
      } else {
        try
        {
          localObject3 = paramContext.getContentResolver().openInputStream((Uri)localObject1);
        }
        catch (Exception localException)
        {
          for (;;)
          {
            localObject3 = new StringBuilder();
            ((StringBuilder)localObject3).append("Unable to load image from URI: ");
            ((StringBuilder)localObject3).append(localObject1);
            Log.w("Icon", ((StringBuilder)localObject3).toString(), localException);
            localObject3 = localFileNotFoundException;
          }
        }
      }
      if (localObject3 != null) {
        return new BitmapDrawable(paramContext.getResources(), BitmapFactory.decodeStream((InputStream)localObject3));
      }
      break;
    case 3: 
      return new BitmapDrawable(paramContext.getResources(), BitmapFactory.decodeByteArray(getDataBytes(), getDataOffset(), getDataLength()));
    case 2: 
      if (getResources() == null)
      {
        Object localObject4 = getResPackage();
        localObject3 = localObject4;
        if (TextUtils.isEmpty((CharSequence)localObject4)) {
          localObject3 = paramContext.getPackageName();
        }
        if ("android".equals(localObject3))
        {
          mObj1 = Resources.getSystem();
        }
        else
        {
          localObject1 = paramContext.getPackageManager();
          try
          {
            localObject4 = ((PackageManager)localObject1).getApplicationInfo((String)localObject3, 8192);
            if (localObject4 != null) {
              mObj1 = ((PackageManager)localObject1).getResourcesForApplication((ApplicationInfo)localObject4);
            }
          }
          catch (PackageManager.NameNotFoundException paramContext)
          {
            Log.e("Icon", String.format("Unable to find pkg=%s for icon %s", new Object[] { localObject3, this }), paramContext);
          }
        }
      }
      try
      {
        paramContext = getResources().getDrawable(getResId(), paramContext.getTheme());
        return paramContext;
      }
      catch (RuntimeException paramContext)
      {
        Log.e("Icon", String.format("Unable to load resource 0x%08x from pkg=%s", new Object[] { Integer.valueOf(getResId()), getResPackage() }), paramContext);
      }
    case 1: 
      return new BitmapDrawable(paramContext.getResources(), getBitmap());
    }
    return null;
  }
  
  public static Bitmap scaleDownIfNecessary(Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    Bitmap localBitmap;
    if (i <= paramInt1)
    {
      localBitmap = paramBitmap;
      if (j <= paramInt2) {}
    }
    else
    {
      float f = Math.min(paramInt1 / i, paramInt2 / j);
      localBitmap = Bitmap.createScaledBitmap(paramBitmap, Math.max(1, (int)(i * f)), Math.max(1, (int)(j * f)), true);
    }
    return localBitmap;
  }
  
  private void setBitmap(Bitmap paramBitmap)
  {
    mObj1 = paramBitmap;
  }
  
  private static final String typeToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "UNKNOWN";
    case 5: 
      return "BITMAP_MASKABLE";
    case 4: 
      return "URI";
    case 3: 
      return "DATA";
    case 2: 
      return "RESOURCE";
    }
    return "BITMAP";
  }
  
  public void convertToAshmem()
  {
    if (((mType == 1) || (mType == 5)) && (getBitmap().isMutable()) && (getBitmap().getAllocationByteCount() >= 131072)) {
      setBitmap(getBitmap().createAshmemBitmap());
    }
  }
  
  public int describeContents()
  {
    int i = mType;
    int j = 1;
    int k = j;
    if (i != 1)
    {
      k = j;
      if (mType != 5) {
        if (mType == 3) {
          k = j;
        } else {
          k = 0;
        }
      }
    }
    return k;
  }
  
  public Bitmap getBitmap()
  {
    if ((mType != 1) && (mType != 5))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("called getBitmap() on ");
      localStringBuilder.append(this);
      throw new IllegalStateException(localStringBuilder.toString());
    }
    return (Bitmap)mObj1;
  }
  
  public byte[] getDataBytes()
  {
    if (mType == 3) {
      try
      {
        byte[] arrayOfByte = (byte[])mObj1;
        return arrayOfByte;
      }
      finally {}
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("called getDataBytes() on ");
    localStringBuilder.append(this);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public int getDataLength()
  {
    if (mType == 3) {
      try
      {
        int i = mInt1;
        return i;
      }
      finally {}
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("called getDataLength() on ");
    localStringBuilder.append(this);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public int getDataOffset()
  {
    if (mType == 3) {
      try
      {
        int i = mInt2;
        return i;
      }
      finally {}
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("called getDataOffset() on ");
    localStringBuilder.append(this);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public int getResId()
  {
    if (mType == 2) {
      return mInt1;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("called getResId() on ");
    localStringBuilder.append(this);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public String getResPackage()
  {
    if (mType == 2) {
      return mString1;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("called getResPackage() on ");
    localStringBuilder.append(this);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public Resources getResources()
  {
    if (mType == 2) {
      return (Resources)mObj1;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("called getResources() on ");
    localStringBuilder.append(this);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  @IconType
  public int getType()
  {
    return mType;
  }
  
  public Uri getUri()
  {
    return Uri.parse(getUriString());
  }
  
  public String getUriString()
  {
    if (mType == 4) {
      return mString1;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("called getUriString() on ");
    localStringBuilder.append(this);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public boolean hasTint()
  {
    boolean bool;
    if ((mTintList == null) && (mTintMode == DEFAULT_TINT_MODE)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public Drawable loadDrawable(Context paramContext)
  {
    paramContext = loadDrawableInner(paramContext);
    if ((paramContext != null) && ((mTintList != null) || (mTintMode != DEFAULT_TINT_MODE)))
    {
      paramContext.mutate();
      paramContext.setTintList(mTintList);
      paramContext.setTintMode(mTintMode);
    }
    return paramContext;
  }
  
  public Drawable loadDrawableAsUser(Context paramContext, int paramInt)
  {
    if (mType == 2)
    {
      Object localObject1 = getResPackage();
      Object localObject2 = localObject1;
      if (TextUtils.isEmpty((CharSequence)localObject1)) {
        localObject2 = paramContext.getPackageName();
      }
      if ((getResources() == null) && (!getResPackage().equals("android")))
      {
        localObject1 = paramContext.getPackageManager();
        try
        {
          mObj1 = ((PackageManager)localObject1).getResourcesForApplicationAsUser((String)localObject2, paramInt);
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          Log.e("Icon", String.format("Unable to find pkg=%s user=%d", new Object[] { getResPackage(), Integer.valueOf(paramInt) }), localNameNotFoundException);
        }
      }
    }
    return loadDrawable(paramContext);
  }
  
  public void loadDrawableAsync(Context paramContext, OnDrawableLoadedListener paramOnDrawableLoadedListener, Handler paramHandler)
  {
    new LoadDrawableTask(paramContext, paramHandler, paramOnDrawableLoadedListener).runAsync();
  }
  
  public void loadDrawableAsync(Context paramContext, Message paramMessage)
  {
    if (paramMessage.getTarget() != null)
    {
      new LoadDrawableTask(paramContext, paramMessage).runAsync();
      return;
    }
    throw new IllegalArgumentException("callback message must have a target handler");
  }
  
  public boolean sameAs(Icon paramIcon)
  {
    boolean bool1 = true;
    boolean bool2 = true;
    boolean bool3 = true;
    if (paramIcon == this) {
      return true;
    }
    if (mType != paramIcon.getType()) {
      return false;
    }
    switch (mType)
    {
    default: 
      return false;
    case 4: 
      return Objects.equals(getUriString(), paramIcon.getUriString());
    case 3: 
      if ((getDataLength() != paramIcon.getDataLength()) || (getDataOffset() != paramIcon.getDataOffset()) || (!Arrays.equals(getDataBytes(), paramIcon.getDataBytes()))) {
        bool3 = false;
      }
      return bool3;
    case 2: 
      if ((getResId() == paramIcon.getResId()) && (Objects.equals(getResPackage(), paramIcon.getResPackage()))) {
        bool3 = bool1;
      } else {
        bool3 = false;
      }
      return bool3;
    }
    if (getBitmap() == paramIcon.getBitmap()) {
      bool3 = bool2;
    } else {
      bool3 = false;
    }
    return bool3;
  }
  
  public void scaleDownIfNecessary(int paramInt1, int paramInt2)
  {
    if ((mType != 1) && (mType != 5)) {
      return;
    }
    setBitmap(scaleDownIfNecessary(getBitmap(), paramInt1, paramInt2));
  }
  
  public Icon setTint(int paramInt)
  {
    return setTintList(ColorStateList.valueOf(paramInt));
  }
  
  public Icon setTintList(ColorStateList paramColorStateList)
  {
    mTintList = paramColorStateList;
    return this;
  }
  
  public Icon setTintMode(PorterDuff.Mode paramMode)
  {
    mTintMode = paramMode;
    return this;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("Icon(typ=").append(typeToString(mType));
    switch (mType)
    {
    default: 
      break;
    case 4: 
      localStringBuilder.append(" uri=");
      localStringBuilder.append(getUriString());
      break;
    case 3: 
      localStringBuilder.append(" len=");
      localStringBuilder.append(getDataLength());
      if (getDataOffset() != 0)
      {
        localStringBuilder.append(" off=");
        localStringBuilder.append(getDataOffset());
      }
      break;
    case 2: 
      localStringBuilder.append(" pkg=");
      localStringBuilder.append(getResPackage());
      localStringBuilder.append(" id=");
      localStringBuilder.append(String.format("0x%08x", new Object[] { Integer.valueOf(getResId()) }));
      break;
    case 1: 
    case 5: 
      localStringBuilder.append(" size=");
      localStringBuilder.append(getBitmap().getWidth());
      localStringBuilder.append("x");
      localStringBuilder.append(getBitmap().getHeight());
    }
    if (mTintList != null)
    {
      localStringBuilder.append(" tint=");
      int[] arrayOfInt = mTintList.getColors();
      int i = arrayOfInt.length;
      String str = "";
      for (int j = 0; j < i; j++)
      {
        localStringBuilder.append(String.format("%s0x%08x", new Object[] { str, Integer.valueOf(arrayOfInt[j]) }));
        str = "|";
      }
    }
    if (mTintMode != DEFAULT_TINT_MODE)
    {
      localStringBuilder.append(" mode=");
      localStringBuilder.append(mTintMode);
    }
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mType);
    switch (mType)
    {
    default: 
      break;
    case 4: 
      paramParcel.writeString(getUriString());
      break;
    case 3: 
      paramParcel.writeInt(getDataLength());
      paramParcel.writeBlob(getDataBytes(), getDataOffset(), getDataLength());
      break;
    case 2: 
      paramParcel.writeString(getResPackage());
      paramParcel.writeInt(getResId());
      break;
    case 1: 
    case 5: 
      getBitmap();
      getBitmap().writeToParcel(paramParcel, paramInt);
    }
    if (mTintList == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(1);
      mTintList.writeToParcel(paramParcel, paramInt);
    }
    paramParcel.writeInt(PorterDuff.modeToInt(mTintMode));
  }
  
  public void writeToStream(OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream = new DataOutputStream(paramOutputStream);
    paramOutputStream.writeInt(1);
    paramOutputStream.writeByte(mType);
    switch (mType)
    {
    default: 
      break;
    case 4: 
      paramOutputStream.writeUTF(getUriString());
      break;
    case 3: 
      paramOutputStream.writeInt(getDataLength());
      paramOutputStream.write(getDataBytes(), getDataOffset(), getDataLength());
      break;
    case 2: 
      paramOutputStream.writeUTF(getResPackage());
      paramOutputStream.writeInt(getResId());
      break;
    case 1: 
    case 5: 
      getBitmap().compress(Bitmap.CompressFormat.PNG, 100, paramOutputStream);
    }
  }
  
  public static @interface IconType {}
  
  private class LoadDrawableTask
    implements Runnable
  {
    final Context mContext;
    final Message mMessage;
    
    public LoadDrawableTask(Context paramContext, Handler paramHandler, final Icon.OnDrawableLoadedListener paramOnDrawableLoadedListener)
    {
      mContext = paramContext;
      mMessage = Message.obtain(paramHandler, new Runnable()
      {
        public void run()
        {
          paramOnDrawableLoadedListener.onDrawableLoaded((Drawable)mMessage.obj);
        }
      });
    }
    
    public LoadDrawableTask(Context paramContext, Message paramMessage)
    {
      mContext = paramContext;
      mMessage = paramMessage;
    }
    
    public void run()
    {
      mMessage.obj = loadDrawable(mContext);
      mMessage.sendToTarget();
    }
    
    public void runAsync()
    {
      AsyncTask.THREAD_POOL_EXECUTOR.execute(this);
    }
  }
  
  public static abstract interface OnDrawableLoadedListener
  {
    public abstract void onDrawableLoaded(Drawable paramDrawable);
  }
}
