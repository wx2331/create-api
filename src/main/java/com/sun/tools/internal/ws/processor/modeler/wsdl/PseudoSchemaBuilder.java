/*     */ package com.sun.tools.internal.ws.processor.modeler.wsdl;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.processor.generator.Names;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wscompile.Options;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Binding;
/*     */ import com.sun.tools.internal.ws.wsdl.document.BindingOperation;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Message;
/*     */ import com.sun.tools.internal.ws.wsdl.document.MessagePart;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Operation;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Port;
/*     */ import com.sun.tools.internal.ws.wsdl.document.PortType;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Service;
/*     */ import com.sun.tools.internal.ws.wsdl.document.WSDLDocument;
/*     */ import com.sun.tools.internal.ws.wsdl.document.jaxws.JAXWSBinding;
/*     */ import com.sun.tools.internal.ws.wsdl.document.schema.SchemaKinds;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAP12Binding;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPBinding;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.AbstractDocument;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.GloballyKnown;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PseudoSchemaBuilder
/*     */ {
/*  56 */   private final StringWriter buf = new StringWriter();
/*     */   private final WSDLDocument wsdlDocument;
/*     */   private WSDLModeler wsdlModeler;
/*  59 */   private final List<InputSource> schemas = new ArrayList<>();
/*  60 */   private final HashMap<QName, Port> bindingNameToPortMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String w3ceprSchemaBinding = "<bindings\n  xmlns=\"http://java.sun.com/xml/ns/jaxb\"\n  xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"\n  xmlns:xjc=\"http://java.sun.com/xml/ns/jaxb/xjc\"\n  version=\"2.1\">\n  \n  <bindings scd=\"x-schema::wsa\" if-exists=\"true\">\n    <bindings scd=\"wsa:EndpointReference\">\n      <class ref=\"javax.xml.ws.wsaddressing.W3CEndpointReference\" xjc:recursive=\"true\"/>\n    </bindings>\n    <bindings scd=\"~wsa:EndpointReferenceType\">\n      <class ref=\"javax.xml.ws.wsaddressing.W3CEndpointReference\" xjc:recursive=\"true\"/>\n    </bindings>\n  </bindings>\n</bindings>";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String memberSubmissionEPR = "<bindings\n  xmlns=\"http://java.sun.com/xml/ns/jaxb\"\n  xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\"\n  version=\"2.1\">\n  \n  <bindings scd=\"x-schema::wsa\" if-exists=\"true\">\n    <bindings scd=\"wsa:EndpointReference\">\n      <class ref=\"com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference\"/>\n    </bindings>\n    <bindings scd=\"~wsa:EndpointReferenceType\">\n      <class ref=\"com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference\"/>\n    </bindings>\n  </bindings>\n</bindings>";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String sysId = "http://dummy.pseudo-schema#schema";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private WsimportOptions options;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean asyncRespBeanBinding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<InputSource> build(WSDLModeler wsdlModeler, WsimportOptions options, ErrorReceiver errReceiver) {
/* 100 */     PseudoSchemaBuilder b = new PseudoSchemaBuilder(wsdlModeler.document);
/* 101 */     b.wsdlModeler = wsdlModeler;
/* 102 */     b.options = options;
/* 103 */     b.build();
/*     */     int i;
/* 105 */     for (i = 0; i < b.schemas.size(); i++) {
/* 106 */       InputSource is = b.schemas.get(i);
/* 107 */       is.setSystemId("http://dummy.pseudo-schema#schema" + (i + 1));
/*     */     } 
/*     */     
/* 110 */     if (!options.noAddressingBbinding && options.target.isLaterThan(Options.Target.V2_1)) {
/* 111 */       InputSource is = new InputSource(new ByteArrayInputStream("<bindings\n  xmlns=\"http://java.sun.com/xml/ns/jaxb\"\n  xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"\n  xmlns:xjc=\"http://java.sun.com/xml/ns/jaxb/xjc\"\n  version=\"2.1\">\n  \n  <bindings scd=\"x-schema::wsa\" if-exists=\"true\">\n    <bindings scd=\"wsa:EndpointReference\">\n      <class ref=\"javax.xml.ws.wsaddressing.W3CEndpointReference\" xjc:recursive=\"true\"/>\n    </bindings>\n    <bindings scd=\"~wsa:EndpointReferenceType\">\n      <class ref=\"javax.xml.ws.wsaddressing.W3CEndpointReference\" xjc:recursive=\"true\"/>\n    </bindings>\n  </bindings>\n</bindings>".getBytes(StandardCharsets.UTF_8)));
/* 112 */       is.setSystemId("http://dummy.pseudo-schema#schema" + (++i + 1));
/* 113 */       b.schemas.add(is);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     return b.schemas;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void build() {
/* 132 */     for (Iterator<Service> itr = this.wsdlDocument.getDefinitions().services(); itr.hasNext();) {
/* 133 */       build(itr.next());
/*     */     }
/*     */   }
/*     */   
/*     */   private void build(Service service) {
/* 138 */     for (Iterator<Port> itr = service.ports(); itr.hasNext();) {
/* 139 */       build(itr.next());
/*     */     }
/*     */   }
/*     */   
/*     */   private void build(Port port) {
/* 144 */     if (this.wsdlModeler.isProvider(port))
/*     */       return; 
/* 146 */     Binding binding = port.resolveBinding((AbstractDocument)this.wsdlDocument);
/*     */ 
/*     */     
/* 149 */     SOAPBinding soapBinding = (SOAPBinding)WSDLModelerBase.getExtensionOfType((TWSDLExtensible)binding, SOAPBinding.class);
/*     */ 
/*     */     
/* 152 */     if (soapBinding == null)
/*     */     {
/* 154 */       soapBinding = (SOAPBinding)WSDLModelerBase.getExtensionOfType((TWSDLExtensible)binding, SOAP12Binding.class);
/*     */     }
/* 156 */     if (soapBinding == null)
/*     */       return; 
/* 158 */     PortType portType = binding.resolvePortType((AbstractDocument)this.wsdlDocument);
/*     */     
/* 160 */     QName bindingName = WSDLModelerBase.getQNameOf((GloballyKnown)binding);
/*     */ 
/*     */     
/* 163 */     if (this.bindingNameToPortMap.containsKey(bindingName)) {
/*     */       return;
/*     */     }
/* 166 */     this.bindingNameToPortMap.put(bindingName, port);
/*     */ 
/*     */     
/* 169 */     for (Iterator<BindingOperation> itr = binding.operations(); itr.hasNext(); ) {
/* 170 */       BindingOperation bindingOperation = itr.next();
/*     */ 
/*     */       
/* 173 */       Set<Operation> boundedOps = portType.getOperationsNamed(bindingOperation.getName());
/* 174 */       if (boundedOps.size() != 1)
/*     */         continue; 
/* 176 */       Operation operation = boundedOps.iterator().next();
/*     */ 
/*     */       
/* 179 */       if (this.wsdlModeler.isAsync(portType, operation)) {
/* 180 */         buildAsync(portType, operation, bindingOperation);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildAsync(PortType portType, Operation operation, BindingOperation bindingOperation) {
/* 191 */     String operationName = getCustomizedOperationName(operation);
/* 192 */     if (operationName == null)
/*     */       return; 
/* 194 */     Message outputMessage = null;
/* 195 */     if (operation.getOutput() != null)
/* 196 */       outputMessage = operation.getOutput().resolveMessage((AbstractDocument)this.wsdlDocument); 
/* 197 */     if (outputMessage != null) {
/* 198 */       List<MessagePart> allParts = new ArrayList<>(outputMessage.getParts());
/* 199 */       if (this.options != null && this.options.additionalHeaders) {
/* 200 */         List<MessagePart> addtionalHeaderParts = this.wsdlModeler.getAdditionHeaderParts(bindingOperation, outputMessage, false);
/* 201 */         allParts.addAll(addtionalHeaderParts);
/*     */       } 
/* 203 */       if (allParts.size() > 1) {
/* 204 */         build(getOperationName(operationName), allParts);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getCustomizedOperationName(Operation operation) {
/* 210 */     JAXWSBinding jaxwsCustomization = (JAXWSBinding)WSDLModelerBase.getExtensionOfType((TWSDLExtensible)operation, JAXWSBinding.class);
/* 211 */     String operationName = (jaxwsCustomization != null) ? ((jaxwsCustomization.getMethodName() != null) ? jaxwsCustomization.getMethodName().getName() : null) : null;
/* 212 */     if (operationName != null) {
/* 213 */       if (Names.isJavaReservedWord(operationName)) {
/* 214 */         return null;
/*     */       }
/*     */       
/* 217 */       return operationName;
/*     */     } 
/* 219 */     return operation.getName();
/*     */   }
/*     */   
/*     */   private void writeImports(QName elementName, List<MessagePart> parts) {
/* 223 */     Set<String> uris = new HashSet<>();
/* 224 */     for (MessagePart p : parts) {
/* 225 */       String ns = p.getDescriptor().getNamespaceURI();
/* 226 */       if (!uris.contains(ns) && !ns.equals("http://www.w3.org/2001/XMLSchema") && !ns.equals(elementName.getNamespaceURI())) {
/* 227 */         print("<xs:import namespace=''{0}''/>", ns);
/* 228 */         uris.add(ns);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private PseudoSchemaBuilder(WSDLDocument _wsdl) {
/* 233 */     this.asyncRespBeanBinding = false;
/*     */     this.wsdlDocument = _wsdl;
/*     */   } private void build(QName elementName, List<MessagePart> allParts) {
/* 236 */     print("<xs:schema xmlns:xs=''http://www.w3.org/2001/XMLSchema''           xmlns:jaxb=''http://java.sun.com/xml/ns/jaxb''           xmlns:xjc=''http://java.sun.com/xml/ns/jaxb/xjc''           jaxb:extensionBindingPrefixes=''xjc''           jaxb:version=''1.0''           targetNamespace=''{0}''>", elementName
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 243 */         .getNamespaceURI());
/*     */     
/* 245 */     writeImports(elementName, allParts);
/*     */     
/* 247 */     if (!this.asyncRespBeanBinding) {
/* 248 */       print("<xs:annotation><xs:appinfo>  <jaxb:schemaBindings>    <jaxb:package name=''{0}'' />  </jaxb:schemaBindings></xs:appinfo></xs:annotation>", this.wsdlModeler
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 254 */           .getJavaPackage());
/* 255 */       this.asyncRespBeanBinding = true;
/*     */     } 
/*     */     
/* 258 */     print("<xs:element name=''{0}''>", elementName.getLocalPart());
/* 259 */     print("<xs:complexType>");
/* 260 */     print("<xs:sequence>");
/*     */ 
/*     */     
/* 263 */     for (MessagePart p : allParts) {
/*     */       
/* 265 */       if (p.getDescriptorKind() == SchemaKinds.XSD_ELEMENT) {
/* 266 */         print("<xs:element ref=''types:{0}'' xmlns:types=''{1}''/>", p.getDescriptor().getLocalPart(), p.getDescriptor().getNamespaceURI()); continue;
/*     */       } 
/* 268 */       print("<xs:element name=''{0}'' type=''{1}'' xmlns=''{2}'' />", p
/* 269 */           .getName(), p
/* 270 */           .getDescriptor().getLocalPart(), p
/* 271 */           .getDescriptor().getNamespaceURI());
/*     */     } 
/*     */ 
/*     */     
/* 275 */     print("</xs:sequence>");
/* 276 */     print("</xs:complexType>");
/* 277 */     print("</xs:element>");
/* 278 */     print("</xs:schema>");
/*     */ 
/*     */     
/* 281 */     if (this.buf.toString().length() > 0) {
/*     */       
/* 283 */       InputSource is = new InputSource(new StringReader(this.buf.toString()));
/* 284 */       this.schemas.add(is);
/* 285 */       this.buf.getBuffer().setLength(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private QName getOperationName(String operationName) {
/* 290 */     if (operationName == null) {
/* 291 */       return null;
/*     */     }
/* 293 */     String namespaceURI = "";
/* 294 */     return new QName(namespaceURI, operationName + "Response");
/*     */   }
/*     */   
/*     */   private void print(String msg) {
/* 298 */     print(msg, new Object[0]);
/*     */   }
/*     */   private void print(String msg, Object arg1) {
/* 301 */     print(msg, new Object[] { arg1 });
/*     */   }
/*     */   private void print(String msg, Object arg1, Object arg2) {
/* 304 */     print(msg, new Object[] { arg1, arg2 });
/*     */   }
/*     */   private void print(String msg, Object arg1, Object arg2, Object arg3) {
/* 307 */     print(msg, new Object[] { arg1, arg2, arg3 });
/*     */   }
/*     */   private void print(String msg, Object[] args) {
/* 310 */     this.buf.write(MessageFormat.format(msg, args));
/* 311 */     this.buf.write(10);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\wsdl\PseudoSchemaBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */