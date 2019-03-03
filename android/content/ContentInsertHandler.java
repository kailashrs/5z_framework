package android.content;

import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public abstract interface ContentInsertHandler
  extends ContentHandler
{
  public abstract void insert(ContentResolver paramContentResolver, InputStream paramInputStream)
    throws IOException, SAXException;
  
  public abstract void insert(ContentResolver paramContentResolver, String paramString)
    throws SAXException;
}
