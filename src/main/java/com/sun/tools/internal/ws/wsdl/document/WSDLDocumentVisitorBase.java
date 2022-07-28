package com.sun.tools.internal.ws.wsdl.document;

import com.sun.tools.internal.ws.wsdl.framework.ExtensionVisitorBase;

public class WSDLDocumentVisitorBase extends ExtensionVisitorBase {
  public void preVisit(Definitions definitions) throws Exception {}
  
  public void postVisit(Definitions definitions) throws Exception {}
  
  public void visit(Import i) throws Exception {}
  
  public void preVisit(Types types) throws Exception {}
  
  public void postVisit(Types types) throws Exception {}
  
  public void preVisit(Message message) throws Exception {}
  
  public void postVisit(Message message) throws Exception {}
  
  public void visit(MessagePart part) throws Exception {}
  
  public void preVisit(PortType portType) throws Exception {}
  
  public void postVisit(PortType portType) throws Exception {}
  
  public void preVisit(Operation operation) throws Exception {}
  
  public void postVisit(Operation operation) throws Exception {}
  
  public void preVisit(Input input) throws Exception {}
  
  public void postVisit(Input input) throws Exception {}
  
  public void preVisit(Output output) throws Exception {}
  
  public void postVisit(Output output) throws Exception {}
  
  public void preVisit(Fault fault) throws Exception {}
  
  public void postVisit(Fault fault) throws Exception {}
  
  public void preVisit(Binding binding) throws Exception {}
  
  public void postVisit(Binding binding) throws Exception {}
  
  public void preVisit(BindingOperation operation) throws Exception {}
  
  public void postVisit(BindingOperation operation) throws Exception {}
  
  public void preVisit(BindingInput input) throws Exception {}
  
  public void postVisit(BindingInput input) throws Exception {}
  
  public void preVisit(BindingOutput output) throws Exception {}
  
  public void postVisit(BindingOutput output) throws Exception {}
  
  public void preVisit(BindingFault fault) throws Exception {}
  
  public void postVisit(BindingFault fault) throws Exception {}
  
  public void preVisit(Service service) throws Exception {}
  
  public void postVisit(Service service) throws Exception {}
  
  public void preVisit(Port port) throws Exception {}
  
  public void postVisit(Port port) throws Exception {}
  
  public void visit(Documentation documentation) throws Exception {}
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\WSDLDocumentVisitorBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */