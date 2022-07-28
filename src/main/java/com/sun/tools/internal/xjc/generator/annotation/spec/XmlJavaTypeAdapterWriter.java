package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import com.sun.codemodel.internal.JType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public interface XmlJavaTypeAdapterWriter extends JAnnotationWriter<XmlJavaTypeAdapter> {
  XmlJavaTypeAdapterWriter type(Class paramClass);
  
  XmlJavaTypeAdapterWriter type(JType paramJType);
  
  XmlJavaTypeAdapterWriter value(Class paramClass);
  
  XmlJavaTypeAdapterWriter value(JType paramJType);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\annotation\spec\XmlJavaTypeAdapterWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */