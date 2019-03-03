package android.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.GraphicBuffer;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.transition.TransitionUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import java.util.List;
import java.util.Map;

public abstract class SharedElementCallback
{
  private static final String BUNDLE_SNAPSHOT_BITMAP = "sharedElement:snapshot:bitmap";
  private static final String BUNDLE_SNAPSHOT_GRAPHIC_BUFFER = "sharedElement:snapshot:graphicBuffer";
  private static final String BUNDLE_SNAPSHOT_IMAGE_MATRIX = "sharedElement:snapshot:imageMatrix";
  private static final String BUNDLE_SNAPSHOT_IMAGE_SCALETYPE = "sharedElement:snapshot:imageScaleType";
  static final SharedElementCallback NULL_CALLBACK = new SharedElementCallback() {};
  private Matrix mTempMatrix;
  
  public SharedElementCallback() {}
  
  public Parcelable onCaptureSharedElementSnapshot(View paramView, Matrix paramMatrix, RectF paramRectF)
  {
    if ((paramView instanceof ImageView))
    {
      ImageView localImageView = (ImageView)paramView;
      Drawable localDrawable = localImageView.getDrawable();
      Object localObject = localImageView.getBackground();
      if ((localDrawable != null) && ((localObject == null) || (((Drawable)localObject).getAlpha() == 0)))
      {
        localObject = TransitionUtils.createDrawableBitmap(localDrawable, localImageView);
        if (localObject != null)
        {
          paramView = new Bundle();
          if (((Bitmap)localObject).getConfig() != Bitmap.Config.HARDWARE) {
            paramView.putParcelable("sharedElement:snapshot:bitmap", (Parcelable)localObject);
          } else {
            paramView.putParcelable("sharedElement:snapshot:graphicBuffer", ((Bitmap)localObject).createGraphicBufferHandle());
          }
          paramView.putString("sharedElement:snapshot:imageScaleType", localImageView.getScaleType().toString());
          if (localImageView.getScaleType() == ImageView.ScaleType.MATRIX)
          {
            paramMatrix = localImageView.getImageMatrix();
            paramRectF = new float[9];
            paramMatrix.getValues(paramRectF);
            paramView.putFloatArray("sharedElement:snapshot:imageMatrix", paramRectF);
          }
          return paramView;
        }
      }
    }
    if (mTempMatrix == null) {
      mTempMatrix = new Matrix(paramMatrix);
    } else {
      mTempMatrix.set(paramMatrix);
    }
    paramMatrix = (ViewGroup)paramView.getParent();
    return TransitionUtils.createViewBitmap(paramView, mTempMatrix, paramRectF, paramMatrix);
  }
  
  public View onCreateSnapshotView(Context paramContext, Parcelable paramParcelable)
  {
    Object localObject1 = null;
    if ((paramParcelable instanceof Bundle))
    {
      Object localObject2 = (Bundle)paramParcelable;
      GraphicBuffer localGraphicBuffer = (GraphicBuffer)((Bundle)localObject2).getParcelable("sharedElement:snapshot:graphicBuffer");
      localObject1 = (Bitmap)((Bundle)localObject2).getParcelable("sharedElement:snapshot:bitmap");
      if ((localGraphicBuffer == null) && (localObject1 == null)) {
        return null;
      }
      paramParcelable = (Parcelable)localObject1;
      if (localObject1 == null) {
        paramParcelable = Bitmap.createHardwareBitmap(localGraphicBuffer);
      }
      paramContext = new ImageView(paramContext);
      localObject1 = paramContext;
      paramContext.setImageBitmap(paramParcelable);
      paramContext.setScaleType(ImageView.ScaleType.valueOf(((Bundle)localObject2).getString("sharedElement:snapshot:imageScaleType")));
      if (paramContext.getScaleType() == ImageView.ScaleType.MATRIX)
      {
        localObject2 = ((Bundle)localObject2).getFloatArray("sharedElement:snapshot:imageMatrix");
        paramParcelable = new Matrix();
        paramParcelable.setValues((float[])localObject2);
        paramContext.setImageMatrix(paramParcelable);
      }
    }
    else if ((paramParcelable instanceof Bitmap))
    {
      paramParcelable = (Bitmap)paramParcelable;
      localObject1 = new View(paramContext);
      ((View)localObject1).setBackground(new BitmapDrawable(paramContext.getResources(), paramParcelable));
    }
    return localObject1;
  }
  
  public void onMapSharedElements(List<String> paramList, Map<String, View> paramMap) {}
  
  public void onRejectSharedElements(List<View> paramList) {}
  
  public void onSharedElementEnd(List<String> paramList, List<View> paramList1, List<View> paramList2) {}
  
  public void onSharedElementStart(List<String> paramList, List<View> paramList1, List<View> paramList2) {}
  
  public void onSharedElementsArrived(List<String> paramList, List<View> paramList1, OnSharedElementsReadyListener paramOnSharedElementsReadyListener)
  {
    paramOnSharedElementsReadyListener.onSharedElementsReady();
  }
  
  public static abstract interface OnSharedElementsReadyListener
  {
    public abstract void onSharedElementsReady();
  }
}
