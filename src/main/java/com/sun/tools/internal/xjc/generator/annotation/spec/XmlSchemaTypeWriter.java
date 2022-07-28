package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import com.sun.codemodel.internal.JType;
import javax.xml.bind.annotation.XmlSchemaType;

public interface XmlSchemaTypeWriter extends JAnnotationWriter<XmlSchemaType> {
  XmlSchemaTypeWriter name(String paramString);
  
  XmlSchemaTypeWriter type(Class paramClass);
  
  XmlSchemaTypeWriter type(JType paramJType);
  
  XmlSchemaTypeWriter namespace(String paramString);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\annotation\spec\XmlSchemaTypeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */