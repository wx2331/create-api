package com.sun.tools.internal.xjc.api;

import java.util.Collection;
import java.util.Map;
import javax.annotation.processing.ProcessingEnvironment;
import javax.xml.namespace.QName;

public interface JavaCompiler {
  J2SJAXBModel bind(Collection<Reference> paramCollection, Map<QName, Reference> paramMap, String paramString, ProcessingEnvironment paramProcessingEnvironment);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\JavaCompiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */