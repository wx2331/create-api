package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlElements;

public interface XmlElementsWriter extends JAnnotationWriter<XmlElements> {
  XmlElementWriter value();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\annotation\spec\XmlElementsWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */