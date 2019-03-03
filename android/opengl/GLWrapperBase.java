package android.opengl;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL10Ext;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;
import javax.microedition.khronos.opengles.GL11ExtensionPack;

abstract class GLWrapperBase
  implements GL, GL10, GL10Ext, GL11, GL11Ext, GL11ExtensionPack
{
  protected GL10 mgl;
  protected GL10Ext mgl10Ext;
  protected GL11 mgl11;
  protected GL11Ext mgl11Ext;
  protected GL11ExtensionPack mgl11ExtensionPack;
  
  public GLWrapperBase(GL paramGL)
  {
    mgl = ((GL10)paramGL);
    if ((paramGL instanceof GL10Ext)) {
      mgl10Ext = ((GL10Ext)paramGL);
    }
    if ((paramGL instanceof GL11)) {
      mgl11 = ((GL11)paramGL);
    }
    if ((paramGL instanceof GL11Ext)) {
      mgl11Ext = ((GL11Ext)paramGL);
    }
    if ((paramGL instanceof GL11ExtensionPack)) {
      mgl11ExtensionPack = ((GL11ExtensionPack)paramGL);
    }
  }
}
