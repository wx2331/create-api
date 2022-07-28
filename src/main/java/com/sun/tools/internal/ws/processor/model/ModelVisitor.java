package com.sun.tools.internal.ws.processor.model;

public interface ModelVisitor {
  void visit(Model paramModel) throws Exception;
  
  void visit(Service paramService) throws Exception;
  
  void visit(Port paramPort) throws Exception;
  
  void visit(Operation paramOperation) throws Exception;
  
  void visit(Request paramRequest) throws Exception;
  
  void visit(Response paramResponse) throws Exception;
  
  void visit(Fault paramFault) throws Exception;
  
  void visit(Block paramBlock) throws Exception;
  
  void visit(Parameter paramParameter) throws Exception;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\ModelVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */