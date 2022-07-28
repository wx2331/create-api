package com.sun.xml.internal.xsom;

import com.sun.xml.internal.xsom.parser.SchemaDocument;
import com.sun.xml.internal.xsom.visitor.XSFunction;
import com.sun.xml.internal.xsom.visitor.XSVisitor;
import java.util.Collection;
import java.util.List;
import javax.xml.namespace.NamespaceContext;
import org.xml.sax.Locator;

public interface XSComponent {
  XSAnnotation getAnnotation();
  
  XSAnnotation getAnnotation(boolean paramBoolean);
  
  List<? extends ForeignAttributes> getForeignAttributes();
  
  String getForeignAttribute(String paramString1, String paramString2);
  
  Locator getLocator();
  
  XSSchema getOwnerSchema();
  
  XSSchemaSet getRoot();
  
  SchemaDocument getSourceDocument();
  
  Collection<XSComponent> select(String paramString, NamespaceContext paramNamespaceContext);
  
  XSComponent selectSingle(String paramString, NamespaceContext paramNamespaceContext);
  
  void visit(XSVisitor paramXSVisitor);
  
  <T> T apply(XSFunction<T> paramXSFunction);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */