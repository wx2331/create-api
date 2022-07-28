package com.sun.tools.internal.xjc.generator.annotation.spec;

import com.sun.codemodel.internal.JAnnotationWriter;
import com.sun.codemodel.internal.JType;
import javax.xml.bind.annotation.XmlElementDecl;

public interface XmlElementDeclWriter extends JAnnotationWriter<XmlElementDecl> {
  XmlElementDeclWriter name(String paramString);
  
  XmlElementDeclWriter scope(Class paramClass);
  
  XmlElementDeclWriter scope(JType paramJType);
  
  XmlElementDeclWriter namespace(String paramString);
  
  XmlElementDeclWriter defaultValue(String paramString);
  
  XmlElementDeclWriter substitutionHeadNamespace(String paramString);
  
  XmlElementDeclWriter substitutionHeadName(String paramString);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\annotation\spec\XmlElementDeclWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */