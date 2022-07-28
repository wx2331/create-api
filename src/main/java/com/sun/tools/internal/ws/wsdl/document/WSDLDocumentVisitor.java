package com.sun.tools.internal.ws.wsdl.document;

import com.sun.tools.internal.ws.wsdl.framework.ExtensionVisitor;

public interface WSDLDocumentVisitor extends ExtensionVisitor {
  void preVisit(Definitions paramDefinitions) throws Exception;
  
  void postVisit(Definitions paramDefinitions) throws Exception;
  
  void visit(Import paramImport) throws Exception;
  
  void preVisit(Types paramTypes) throws Exception;
  
  void postVisit(Types paramTypes) throws Exception;
  
  void preVisit(Message paramMessage) throws Exception;
  
  void postVisit(Message paramMessage) throws Exception;
  
  void visit(MessagePart paramMessagePart) throws Exception;
  
  void preVisit(PortType paramPortType) throws Exception;
  
  void postVisit(PortType paramPortType) throws Exception;
  
  void preVisit(Operation paramOperation) throws Exception;
  
  void postVisit(Operation paramOperation) throws Exception;
  
  void preVisit(Input paramInput) throws Exception;
  
  void postVisit(Input paramInput) throws Exception;
  
  void preVisit(Output paramOutput) throws Exception;
  
  void postVisit(Output paramOutput) throws Exception;
  
  void preVisit(Fault paramFault) throws Exception;
  
  void postVisit(Fault paramFault) throws Exception;
  
  void preVisit(Binding paramBinding) throws Exception;
  
  void postVisit(Binding paramBinding) throws Exception;
  
  void preVisit(BindingOperation paramBindingOperation) throws Exception;
  
  void postVisit(BindingOperation paramBindingOperation) throws Exception;
  
  void preVisit(BindingInput paramBindingInput) throws Exception;
  
  void postVisit(BindingInput paramBindingInput) throws Exception;
  
  void preVisit(BindingOutput paramBindingOutput) throws Exception;
  
  void postVisit(BindingOutput paramBindingOutput) throws Exception;
  
  void preVisit(BindingFault paramBindingFault) throws Exception;
  
  void postVisit(BindingFault paramBindingFault) throws Exception;
  
  void preVisit(Service paramService) throws Exception;
  
  void postVisit(Service paramService) throws Exception;
  
  void preVisit(Port paramPort) throws Exception;
  
  void postVisit(Port paramPort) throws Exception;
  
  void visit(Documentation paramDocumentation) throws Exception;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\WSDLDocumentVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */