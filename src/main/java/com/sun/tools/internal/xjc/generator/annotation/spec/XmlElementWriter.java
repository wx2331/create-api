package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import com.sun.codemodel.internal.JType;
import javax.xml.bind.annotation.XmlElement;

public interface XmlElementWriter extends JAnnotationWriter<XmlElement> {
  XmlElementWriter name(String paramString);
  
  XmlElementWriter type(Class paramClass);
  
  XmlElementWriter type(JType paramJType);
  
  XmlElementWriter namespace(String paramString);
  
  XmlElementWriter defaultValue(String paramString);
  
  XmlElementWriter required(boolean paramBoolean);
  
  XmlElementWriter nillable(boolean paramBoolean);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\annotation\spec\XmlElementWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */