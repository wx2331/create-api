package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

public interface XmlAccessorTypeWriter extends JAnnotationWriter<XmlAccessorType> {
  XmlAccessorTypeWriter value(XmlAccessType paramXmlAccessType);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\annotation\spec\XmlAccessorTypeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */