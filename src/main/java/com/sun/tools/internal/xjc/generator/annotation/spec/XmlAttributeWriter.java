package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlAttribute;

public interface XmlAttributeWriter extends JAnnotationWriter<XmlAttribute> {
  XmlAttributeWriter name(String paramString);
  
  XmlAttributeWriter namespace(String paramString);
  
  XmlAttributeWriter required(boolean paramBoolean);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\annotation\spec\XmlAttributeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */