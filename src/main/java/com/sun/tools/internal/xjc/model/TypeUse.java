package com.sun.tools.internal.xjc.model;

import com.sun.codemodel.internal.JExpression;
import com.sun.tools.internal.xjc.outline.Outline;
import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sun.xml.internal.xsom.XmlString;
import javax.activation.MimeType;

public interface TypeUse {
  boolean isCollection();
  
  CAdapter getAdapterUse();
  
  CNonElement getInfo();
  
  ID idUse();
  
  MimeType getExpectedMimeType();
  
  JExpression createConstant(Outline paramOutline, XmlString paramXmlString);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\TypeUse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */