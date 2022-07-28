package com.sun.xml.internal.xsom.parser;

import com.sun.xml.internal.xsom.XSSchema;
import java.util.Set;

public interface SchemaDocument {
  String getSystemId();
  
  String getTargetNamespace();
  
  XSSchema getSchema();
  
  Set<SchemaDocument> getReferencedDocuments();
  
  Set<SchemaDocument> getIncludedDocuments();
  
  Set<SchemaDocument> getImportedDocuments(String paramString);
  
  boolean includes(SchemaDocument paramSchemaDocument);
  
  boolean imports(SchemaDocument paramSchemaDocument);
  
  Set<SchemaDocument> getReferers();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\parser\SchemaDocument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */