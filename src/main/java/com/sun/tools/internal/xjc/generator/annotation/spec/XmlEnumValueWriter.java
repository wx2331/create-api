package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlEnumValue;

public interface XmlEnumValueWriter extends JAnnotationWriter<XmlEnumValue> {
  XmlEnumValueWriter value(String paramString);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\annotation\spec\XmlEnumValueWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */