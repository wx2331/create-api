package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import com.sun.codemodel.internal.JType;
import javax.xml.bind.annotation.XmlType;

public interface XmlTypeWriter extends JAnnotationWriter<XmlType> {
  XmlTypeWriter name(String paramString);
  
  XmlTypeWriter namespace(String paramString);
  
  XmlTypeWriter propOrder(String paramString);
  
  XmlTypeWriter factoryClass(Class paramClass);
  
  XmlTypeWriter factoryClass(JType paramJType);
  
  XmlTypeWriter factoryMethod(String paramString);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\annotation\spec\XmlTypeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */