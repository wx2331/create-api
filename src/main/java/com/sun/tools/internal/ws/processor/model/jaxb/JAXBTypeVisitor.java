package com.sun.tools.internal.ws.processor.model.jaxb;

public interface JAXBTypeVisitor {
  void visit(JAXBType paramJAXBType) throws Exception;
  
  void visit(RpcLitStructure paramRpcLitStructure) throws Exception;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\jaxb\JAXBTypeVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */