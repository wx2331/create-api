package com.sun.tools.internal.ws.processor.modeler.annotation;

import com.sun.tools.internal.ws.processor.modeler.ModelerException;
import com.sun.tools.internal.ws.wscompile.WsgenOptions;
import java.io.File;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public interface ModelBuilder {
  ProcessingEnvironment getProcessingEnvironment();
  
  String getOperationName(Name paramName);
  
  TypeMirror getHolderValueType(TypeMirror paramTypeMirror);
  
  boolean checkAndSetProcessed(TypeElement paramTypeElement);
  
  boolean isServiceException(TypeMirror paramTypeMirror);
  
  boolean isRemote(TypeElement paramTypeElement);
  
  boolean canOverWriteClass(String paramString);
  
  WsgenOptions getOptions();
  
  File getSourceDir();
  
  void log(String paramString);
  
  void processWarning(String paramString);
  
  void processError(String paramString);
  
  void processError(String paramString, Element paramElement) throws ModelerException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\annotation\ModelBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */