package android.text.style;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import java.io.InputStream;

public class ImageSpan
  extends DynamicDrawableSpan
{
  private Uri mContentUri;
  private Context mContext;
  private Drawable mDrawable;
  private int mResourceId;
  private String mSource;
  
  public ImageSpan(Context paramContext, int paramInt)
  {
    this(paramContext, paramInt, 0);
  }
  
  public ImageSpan(Context paramContext, int paramInt1, int paramInt2)
  {
    super(paramInt2);
    mContext = paramContext;
    mResourceId = paramInt1;
  }
  
  public ImageSpan(Context paramContext, Bitmap paramBitmap)
  {
    this(paramContext, paramBitmap, 0);
  }
  
  public ImageSpan(Context paramContext, Bitmap paramBitmap, int paramInt)
  {
    super(paramInt);
    mContext = paramContext;
    if (paramContext != null) {
      paramContext = new BitmapDrawable(paramContext.getResources(), paramBitmap);
    } else {
      paramContext = new BitmapDrawable(paramBitmap);
    }
    mDrawable = paramContext;
    paramInt = mDrawable.getIntrinsicWidth();
    int i = mDrawable.getIntrinsicHeight();
    paramContext = mDrawable;
    if (paramInt <= 0) {
      paramInt = 0;
    }
    if (i <= 0) {
      i = 0;
    }
    paramContext.setBounds(0, 0, paramInt, i);
  }
  
  public ImageSpan(Context paramContext, Uri paramUri)
  {
    this(paramContext, paramUri, 0);
  }
  
  public ImageSpan(Context paramContext, Uri paramUri, int paramInt)
  {
    super(paramInt);
    mContext = paramContext;
    mContentUri = paramUri;
    mSource = paramUri.toString();
  }
  
  @Deprecated
  public ImageSpan(Bitmap paramBitmap)
  {
    this(null, paramBitmap, 0);
  }
  
  @Deprecated
  public ImageSpan(Bitmap paramBitmap, int paramInt)
  {
    this(null, paramBitmap, paramInt);
  }
  
  public ImageSpan(Drawable paramDrawable)
  {
    this(paramDrawable, 0);
  }
  
  public ImageSpan(Drawable paramDrawable, int paramInt)
  {
    super(paramInt);
    mDrawable = paramDrawable;
  }
  
  public ImageSpan(Drawable paramDrawable, String paramString)
  {
    this(paramDrawable, paramString, 0);
  }
  
  public ImageSpan(Drawable paramDrawable, String paramString, int paramInt)
  {
    super(paramInt);
    mDrawable = paramDrawable;
    mSource = paramString;
  }
  
  public Drawable getDrawable()
  {
    Object localObject1 = null;
    Object localObject2 = null;
    if (mDrawable != null)
    {
      localObject1 = mDrawable;
    }
    else if (mContentUri != null)
    {
      localObject1 = localObject2;
      try
      {
        InputStream localInputStream = mContext.getContentResolver().openInputStream(mContentUri);
        localObject1 = localObject2;
        Bitmap localBitmap = BitmapFactory.decodeStream(localInputStream);
        localObject1 = localObject2;
        localObject3 = new android/graphics/drawable/BitmapDrawable;
        localObject1 = localObject2;
        ((BitmapDrawable)localObject3).<init>(mContext.getResources(), localBitmap);
        localObject2 = localObject3;
        localObject1 = localObject2;
        localObject2.setBounds(0, 0, localObject2.getIntrinsicWidth(), localObject2.getIntrinsicHeight());
        localObject1 = localObject2;
        localInputStream.close();
        localObject1 = localObject2;
      }
      catch (Exception localException1)
      {
        Object localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("Failed to loaded content ");
        ((StringBuilder)localObject3).append(mContentUri);
        Log.e("ImageSpan", ((StringBuilder)localObject3).toString(), localException1);
      }
    }
    else
    {
      try
      {
        Drawable localDrawable = mContext.getDrawable(mResourceId);
        localObject1 = localDrawable;
        localDrawable.setBounds(0, 0, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight());
        localObject1 = localDrawable;
      }
      catch (Exception localException2)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unable to find resource: ");
        localStringBuilder.append(mResourceId);
        Log.e("ImageSpan", localStringBuilder.toString());
      }
    }
    return localObject1;
  }
  
  public String getSource()
  {
    return mSource;
  }
}
