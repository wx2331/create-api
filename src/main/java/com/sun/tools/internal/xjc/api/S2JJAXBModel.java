package com.sun.tools.internal.xjc.api;

import com.sun.codemodel.internal.JClass;
import com.sun.codemodel.internal.JCodeModel;
import com.sun.tools.internal.xjc.Plugin;
import java.util.Collection;
import java.util.List;
import javax.xml.namespace.QName;

public interface S2JJAXBModel extends JAXBModel {
  Mapping get(QName paramQName);
  
  List<JClass> getAllObjectFactories();
  
  Collection<? extends Mapping> getMappings();
  
  TypeAndAnnotation getJavaType(QName paramQName);
  
  JCodeModel generateCode(Plugin[] paramArrayOfPlugin, ErrorListener paramErrorListener);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\S2JJAXBModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */