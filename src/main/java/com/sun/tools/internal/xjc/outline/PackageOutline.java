package com.sun.tools.internal.xjc.outline;

import com.sun.codemodel.internal.JDefinedClass;
import com.sun.codemodel.internal.JPackage;
import com.sun.tools.internal.xjc.generator.bean.ObjectFactoryGenerator;
import java.util.Set;
import javax.xml.bind.annotation.XmlNsForm;

public interface PackageOutline {
  JPackage _package();
  
  JDefinedClass objectFactory();
  
  ObjectFactoryGenerator objectFactoryGenerator();
  
  Set<? extends ClassOutline> getClasses();
  
  String getMostUsedNamespaceURI();
  
  XmlNsForm getElementFormDefault();
  
  XmlNsForm getAttributeFormDefault();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\outline\PackageOutline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */