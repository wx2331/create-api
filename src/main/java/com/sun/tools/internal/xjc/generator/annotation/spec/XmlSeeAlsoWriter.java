package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import com.sun.codemodel.internal.JType;
import javax.xml.bind.annotation.XmlSeeAlso;

public interface XmlSeeAlsoWriter extends JAnnotationWriter<XmlSeeAlso> {
  XmlSeeAlsoWriter value(Class paramClass);
  
  XmlSeeAlsoWriter value(JType paramJType);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\annotation\spec\XmlSeeAlsoWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */