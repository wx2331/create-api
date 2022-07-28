package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;

public interface XmlSchemaWriter extends JAnnotationWriter<XmlSchema> {
  XmlSchemaWriter location(String paramString);
  
  XmlSchemaWriter namespace(String paramString);
  
  XmlNsWriter xmlns();
  
  XmlSchemaWriter elementFormDefault(XmlNsForm paramXmlNsForm);
  
  XmlSchemaWriter attributeFormDefault(XmlNsForm paramXmlNsForm);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\annotation\spec\XmlSchemaWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */