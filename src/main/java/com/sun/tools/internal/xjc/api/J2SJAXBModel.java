package com.sun.tools.internal.xjc.api;

import java.io.IOException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;

public interface J2SJAXBModel extends JAXBModel {
  QName getXmlTypeName(Reference paramReference);
  
  void generateSchema(SchemaOutputResolver paramSchemaOutputResolver, ErrorListener paramErrorListener) throws IOException;
  
  void generateEpisodeFile(Result paramResult);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\J2SJAXBModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */