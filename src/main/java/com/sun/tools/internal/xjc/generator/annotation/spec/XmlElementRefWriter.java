package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import com.sun.codemodel.internal.JType;
import javax.xml.bind.annotation.XmlElementRef;

public interface XmlElementRefWriter extends JAnnotationWriter<XmlElementRef> {
  XmlElementRefWriter name(String paramString);
  
  XmlElementRefWriter type(Class paramClass);
  
  XmlElementRefWriter type(JType paramJType);
  
  XmlElementRefWriter namespace(String paramString);
  
  XmlElementRefWriter required(boolean paramBoolean);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\annotation\spec\XmlElementRefWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */