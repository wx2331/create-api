/*     */ package com.sun.tools.internal.ws.processor.modeler.wsdl;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.processor.generator.Names;
/*     */ import com.sun.tools.internal.ws.processor.model.Fault;
/*     */ import com.sun.tools.internal.ws.processor.model.Operation;
/*     */ import com.sun.tools.internal.ws.processor.model.Port;
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaException;
/*     */ import com.sun.tools.internal.ws.processor.modeler.Modeler;
/*     */ import com.sun.tools.internal.ws.resources.ModelerMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.AbortException;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiverFilter;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*     */ import com.sun.tools.internal.ws.wsdl.document.BindingFault;
/*     */ import com.sun.tools.internal.ws.wsdl.document.BindingOperation;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Fault;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Kinds;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Message;
/*     */ import com.sun.tools.internal.ws.wsdl.document.MessagePart;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Operation;
/*     */ import com.sun.tools.internal.ws.wsdl.document.OperationStyle;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Port;
/*     */ import com.sun.tools.internal.ws.wsdl.document.WSDLDocument;
/*     */ import com.sun.tools.internal.ws.wsdl.document.jaxws.JAXWSBinding;
/*     */ import com.sun.tools.internal.ws.wsdl.document.mime.MIMEContent;
/*     */ import com.sun.tools.internal.ws.wsdl.document.mime.MIMEMultipartRelated;
/*     */ import com.sun.tools.internal.ws.wsdl.document.mime.MIMEPart;
/*     */ import com.sun.tools.internal.ws.wsdl.document.schema.SchemaKinds;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPBinding;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPBody;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPFault;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPHeader;
/*     */ import com.sun.tools.internal.ws.wsdl.document.soap.SOAPOperation;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.AbstractDocument;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.GloballyKnown;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.NoSuchEntityException;
/*     */ import com.sun.tools.internal.ws.wsdl.parser.MetadataFinder;
/*     */ import com.sun.tools.internal.ws.wsdl.parser.WSDLParser;
/*     */ import com.sun.tools.internal.xjc.api.ErrorListener;
/*     */ import com.sun.xml.internal.ws.spi.db.BindingHelper;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.helpers.LocatorImpl;
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
/*     */ public abstract class WSDLModelerBase
/*     */   implements Modeler
/*     */ {
/*     */   protected final ErrorReceiverFilter errReceiver;
/*     */   protected final WsimportOptions options;
/*     */   protected MetadataFinder forest;
/*     */   int numPasses;
/*     */   protected static final String OPERATION_HAS_VOID_RETURN_TYPE = "com.sun.xml.internal.ws.processor.modeler.wsdl.operationHasVoidReturnType";
/*     */   protected static final String WSDL_PARAMETER_ORDER = "com.sun.xml.internal.ws.processor.modeler.wsdl.parameterOrder";
/*     */   public static final String WSDL_RESULT_PARAMETER = "com.sun.xml.internal.ws.processor.modeler.wsdl.resultParameter";
/*     */   public static final String MESSAGE_HAS_MIME_MULTIPART_RELATED_BINDING = "com.sun.xml.internal.ws.processor.modeler.wsdl.mimeMultipartRelatedBinding";
/*     */   protected ProcessSOAPOperationInfo info;
/*     */   private Set _conflictingClassNames;
/*     */   protected Map<String, JavaException> _javaExceptions;
/*     */   protected Map _faultTypeToStructureMap;
/*     */   protected Map<QName, Port> _bindingNameToPortMap;
/*     */   private final Set<String> reqResNames;
/*     */   protected WSDLParser parser;
/*     */   protected WSDLDocument document;
/*     */   
/*     */   protected void applyPortMethodCustomization(Port port, Port wsdlPort) {
/*     */     if (isProvider(wsdlPort))
/*     */       return; 
/*     */     JAXWSBinding jaxwsBinding = (JAXWSBinding)getExtensionOfType((TWSDLExtensible)wsdlPort, JAXWSBinding.class);
/*     */     String portMethodName = (jaxwsBinding != null) ? ((jaxwsBinding.getMethodName() != null) ? jaxwsBinding.getMethodName().getName() : null) : null;
/*     */     if (portMethodName != null) {
/*     */       port.setPortGetter(portMethodName);
/*     */     } else {
/*     */       portMethodName = Names.getPortName(port);
/*     */       portMethodName = BindingHelper.mangleNameToClassName(portMethodName);
/*     */       port.setPortGetter("get" + portMethodName);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isProvider(Port wsdlPort) {
/*     */     JAXWSBinding portCustomization = (JAXWSBinding)getExtensionOfType((TWSDLExtensible)wsdlPort, JAXWSBinding.class);
/*     */     Boolean isProvider = (portCustomization != null) ? portCustomization.isProvider() : null;
/*     */     if (isProvider != null)
/*     */       return isProvider.booleanValue(); 
/*     */     JAXWSBinding jaxwsGlobalCustomization = (JAXWSBinding)getExtensionOfType((TWSDLExtensible)this.document.getDefinitions(), JAXWSBinding.class);
/*     */     isProvider = (jaxwsGlobalCustomization != null) ? jaxwsGlobalCustomization.isProvider() : null;
/*     */     if (isProvider != null)
/*     */       return isProvider.booleanValue(); 
/*     */     return false;
/*     */   }
/*     */   
/*     */   protected SOAPBody getSOAPRequestBody() {
/*     */     SOAPBody requestBody = (SOAPBody)getAnyExtensionOfType((TWSDLExtensible)this.info.bindingOperation.getInput(), SOAPBody.class);
/*     */     if (requestBody == null)
/*     */       error((Entity)this.info.bindingOperation.getInput(), ModelerMessages.WSDLMODELER_INVALID_BINDING_OPERATION_INPUT_MISSING_SOAP_BODY(this.info.bindingOperation.getName())); 
/*     */     return requestBody;
/*     */   }
/*     */   
/*     */   protected boolean isRequestMimeMultipart() {
/*     */     for (TWSDLExtension extension : this.info.bindingOperation.getInput().extensions()) {
/*     */       if (extension.getClass().equals(MIMEMultipartRelated.class))
/*     */         return true; 
/*     */     } 
/*     */     return false;
/*     */   }
/*     */   
/*     */   protected boolean isResponseMimeMultipart() {
/*     */     for (TWSDLExtension extension : this.info.bindingOperation.getOutput().extensions()) {
/*     */       if (extension.getClass().equals(MIMEMultipartRelated.class))
/*     */         return true; 
/*     */     } 
/*     */     return false;
/*     */   }
/*     */   
/*     */   protected SOAPBody getSOAPResponseBody() {
/*     */     SOAPBody responseBody = (SOAPBody)getAnyExtensionOfType((TWSDLExtensible)this.info.bindingOperation.getOutput(), SOAPBody.class);
/*     */     if (responseBody == null)
/*     */       error((Entity)this.info.bindingOperation.getOutput(), ModelerMessages.WSDLMODELER_INVALID_BINDING_OPERATION_OUTPUT_MISSING_SOAP_BODY(this.info.bindingOperation.getName())); 
/*     */     return responseBody;
/*     */   }
/*     */   
/*     */   protected Message getOutputMessage() {
/*     */     if (this.info.portTypeOperation.getOutput() == null)
/*     */       return null; 
/*     */     return this.info.portTypeOperation.getOutput().resolveMessage((AbstractDocument)this.info.document);
/*     */   }
/*     */   
/*     */   protected Message getInputMessage() {
/*     */     return this.info.portTypeOperation.getInput().resolveMessage((AbstractDocument)this.info.document);
/*     */   }
/*     */   
/*     */   protected List<MessagePart> getMessageParts(SOAPBody body, Message message, boolean isInput) {
/*     */     List<MessagePart> mimeParts;
/*     */     String bodyParts = body.getParts();
/*     */     ArrayList<MessagePart> partsList = new ArrayList<>();
/*     */     List<MessagePart> parts = new ArrayList<>();
/*     */     if (isInput) {
/*     */       mimeParts = getMimeContentParts(message, (TWSDLExtensible)this.info.bindingOperation.getInput());
/*     */     } else {
/*     */       mimeParts = getMimeContentParts(message, (TWSDLExtensible)this.info.bindingOperation.getOutput());
/*     */     } 
/*     */     if (bodyParts != null) {
/*     */       StringTokenizer in = new StringTokenizer(bodyParts.trim(), " ");
/*     */       while (in.hasMoreTokens()) {
/*     */         String part = in.nextToken();
/*     */         MessagePart mPart = message.getPart(part);
/*     */         if (null == mPart)
/*     */           error((Entity)message, ModelerMessages.WSDLMODELER_ERROR_PARTS_NOT_FOUND(part, message.getName())); 
/*     */         mPart.setBindingExtensibilityElementKind(1);
/*     */         partsList.add(mPart);
/*     */       } 
/*     */     } else {
/*     */       for (MessagePart mPart : message.getParts()) {
/*     */         if (!mimeParts.contains(mPart))
/*     */           mPart.setBindingExtensibilityElementKind(1); 
/*     */         partsList.add(mPart);
/*     */       } 
/*     */     } 
/*     */     for (MessagePart mPart : message.getParts()) {
/*     */       if (mimeParts.contains(mPart)) {
/*     */         mPart.setBindingExtensibilityElementKind(5);
/*     */         parts.add(mPart);
/*     */         continue;
/*     */       } 
/*     */       if (partsList.contains(mPart)) {
/*     */         mPart.setBindingExtensibilityElementKind(1);
/*     */         parts.add(mPart);
/*     */       } 
/*     */     } 
/*     */     return parts;
/*     */   }
/*     */   
/*     */   protected List<MessagePart> getMimeContentParts(Message message, TWSDLExtensible ext) {
/*     */     ArrayList<MessagePart> mimeContentParts = new ArrayList<>();
/*     */     for (MIMEPart mimePart : getMimeParts(ext)) {
/*     */       MessagePart part = getMimeContentPart(message, mimePart);
/*     */       if (part != null)
/*     */         mimeContentParts.add(part); 
/*     */     } 
/*     */     return mimeContentParts;
/*     */   }
/*     */   
/*     */   protected boolean validateMimeParts(Iterable<MIMEPart> mimeParts) {
/*     */     boolean gotRootPart = false;
/*     */     List<MIMEContent> mimeContents = new ArrayList<>();
/*     */     for (MIMEPart mPart : mimeParts) {
/*     */       for (TWSDLExtension obj : mPart.extensions()) {
/*     */         if (obj instanceof SOAPBody) {
/*     */           if (gotRootPart) {
/*     */             warning((Entity)mPart, ModelerMessages.MIMEMODELER_INVALID_MIME_PART_MORE_THAN_ONE_SOAP_BODY(this.info.operation.getName().getLocalPart()));
/*     */             return false;
/*     */           } 
/*     */           gotRootPart = true;
/*     */           continue;
/*     */         } 
/*     */         if (obj instanceof MIMEContent)
/*     */           mimeContents.add((MIMEContent)obj); 
/*     */       } 
/*     */       if (!validateMimeContentPartNames(mimeContents))
/*     */         return false; 
/*     */       if (mPart.getName() != null)
/*     */         warning((Entity)mPart, ModelerMessages.MIMEMODELER_INVALID_MIME_PART_NAME_NOT_ALLOWED(this.info.portTypeOperation.getName())); 
/*     */     } 
/*     */     return true;
/*     */   }
/*     */   
/*     */   private MessagePart getMimeContentPart(Message message, MIMEPart part) {
/*     */     Iterator<MIMEContent> iterator = getMimeContents(part).iterator();
/*     */     if (iterator.hasNext()) {
/*     */       MIMEContent mimeContent = iterator.next();
/*     */       String mimeContentPartName = mimeContent.getPart();
/*     */       MessagePart mPart = message.getPart(mimeContentPartName);
/*     */       if (null == mPart)
/*     */         error((Entity)mimeContent, ModelerMessages.WSDLMODELER_ERROR_PARTS_NOT_FOUND(mimeContentPartName, message.getName())); 
/*     */       mPart.setBindingExtensibilityElementKind(5);
/*     */       return mPart;
/*     */     } 
/*     */     return null;
/*     */   }
/*     */   
/*     */   protected List<String> getAlternateMimeTypes(List<MIMEContent> mimeContents) {
/*     */     List<String> mimeTypes = new ArrayList<>();
/*     */     for (MIMEContent mimeContent : mimeContents) {
/*     */       String mimeType = getMimeContentType(mimeContent);
/*     */       if (!mimeTypes.contains(mimeType))
/*     */         mimeTypes.add(mimeType); 
/*     */     } 
/*     */     return mimeTypes;
/*     */   }
/*     */   
/*     */   private boolean validateMimeContentPartNames(List<MIMEContent> mimeContents) {
/*     */     for (MIMEContent mimeContent : mimeContents) {
/*     */       String mimeContnetPart = getMimeContentPartName(mimeContent);
/*     */       if (mimeContnetPart == null) {
/*     */         warning((Entity)mimeContent, ModelerMessages.MIMEMODELER_INVALID_MIME_CONTENT_MISSING_PART_ATTRIBUTE(this.info.operation.getName().getLocalPart()));
/*     */         return false;
/*     */       } 
/*     */     } 
/*     */     return true;
/*     */   }
/*     */   
/*     */   protected Iterable<MIMEPart> getMimeParts(TWSDLExtensible ext) {
/*     */     MIMEMultipartRelated multiPartRelated = (MIMEMultipartRelated)getAnyExtensionOfType(ext, MIMEMultipartRelated.class);
/*     */     if (multiPartRelated == null)
/*     */       return Collections.emptyList(); 
/*     */     return multiPartRelated.getParts();
/*     */   }
/*     */   
/*     */   protected List<MIMEContent> getMimeContents(MIMEPart part) {
/*     */     List<MIMEContent> mimeContents = new ArrayList<>();
/*     */     for (TWSDLExtension mimeContent : part.extensions()) {
/*     */       if (mimeContent instanceof MIMEContent)
/*     */         mimeContents.add((MIMEContent)mimeContent); 
/*     */     } 
/*     */     return mimeContents;
/*     */   }
/*     */   
/*     */   private String getMimeContentPartName(MIMEContent mimeContent) {
/*     */     return mimeContent.getPart();
/*     */   }
/*     */   
/*     */   private String getMimeContentType(MIMEContent mimeContent) {
/*     */     String mimeType = mimeContent.getType();
/*     */     if (mimeType == null)
/*     */       error((Entity)mimeContent, ModelerMessages.MIMEMODELER_INVALID_MIME_CONTENT_MISSING_TYPE_ATTRIBUTE(this.info.operation.getName().getLocalPart())); 
/*     */     return mimeType;
/*     */   }
/*     */   
/*     */   protected boolean isStyleAndPartMatch(SOAPOperation soapOperation, MessagePart part) {
/*     */     if (soapOperation != null && soapOperation.getStyle() != null) {
/*     */       if ((soapOperation.isDocument() && part.getDescriptorKind() != SchemaKinds.XSD_ELEMENT) || (soapOperation.isRPC() && part.getDescriptorKind() != SchemaKinds.XSD_TYPE))
/*     */         return false; 
/*     */     } else if ((this.info.soapBinding.isDocument() && part.getDescriptorKind() != SchemaKinds.XSD_ELEMENT) || (this.info.soapBinding.isRPC() && part.getDescriptorKind() != SchemaKinds.XSD_TYPE)) {
/*     */       return false;
/*     */     } 
/*     */     return true;
/*     */   }
/*     */   
/*     */   protected String getRequestNamespaceURI(SOAPBody body) {
/*     */     String namespaceURI = body.getNamespace();
/*     */     if (namespaceURI == null) {
/*     */       if (this.options.isExtensionMode())
/*     */         return this.info.modelPort.getName().getNamespaceURI(); 
/*     */       error((Entity)body, ModelerMessages.WSDLMODELER_INVALID_BINDING_OPERATION_INPUT_SOAP_BODY_MISSING_NAMESPACE(this.info.bindingOperation.getName()));
/*     */     } 
/*     */     return namespaceURI;
/*     */   }
/*     */   
/*     */   protected String getResponseNamespaceURI(SOAPBody body) {
/*     */     String namespaceURI = body.getNamespace();
/*     */     if (namespaceURI == null) {
/*     */       if (this.options.isExtensionMode())
/*     */         return this.info.modelPort.getName().getNamespaceURI(); 
/*     */       error((Entity)body, ModelerMessages.WSDLMODELER_INVALID_BINDING_OPERATION_OUTPUT_SOAP_BODY_MISSING_NAMESPACE(this.info.bindingOperation.getName()));
/*     */     } 
/*     */     return namespaceURI;
/*     */   }
/*     */   
/*     */   protected List<SOAPHeader> getHeaderExtensions(TWSDLExtensible extensible) {
/*     */     List<SOAPHeader> headerList = new ArrayList<>();
/*     */     for (TWSDLExtension extension : extensible.extensions()) {
/*     */       if (extension.getClass() == MIMEMultipartRelated.class) {
/*     */         for (MIMEPart part : ((MIMEMultipartRelated)extension).getParts()) {
/*     */           boolean isRootPart = isRootPart(part);
/*     */           for (TWSDLExtension obj : part.extensions()) {
/*     */             if (obj instanceof SOAPHeader) {
/*     */               if (!isRootPart) {
/*     */                 warning((Entity)obj, ModelerMessages.MIMEMODELER_WARNING_IGNORINGINVALID_HEADER_PART_NOT_DECLARED_IN_ROOT_PART(this.info.bindingOperation.getName()));
/*     */                 return new ArrayList<>();
/*     */               } 
/*     */               headerList.add((SOAPHeader)obj);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         continue;
/*     */       } 
/*     */       if (extension instanceof SOAPHeader)
/*     */         headerList.add((SOAPHeader)extension); 
/*     */     } 
/*     */     return headerList;
/*     */   }
/*     */   
/*     */   private boolean isRootPart(MIMEPart part) {
/*     */     for (TWSDLExtension twsdlExtension : part.extensions()) {
/*     */       if (twsdlExtension instanceof SOAPBody)
/*     */         return true; 
/*     */     } 
/*     */     return false;
/*     */   }
/*     */   
/*     */   protected Set getDuplicateFaultNames() {
/*     */     Set<QName> faultNames = new HashSet<>();
/*     */     Set<QName> duplicateNames = new HashSet<>();
/*     */     for (BindingFault bindingFault : this.info.bindingOperation.faults()) {
/*     */       Fault portTypeFault = null;
/*     */       for (Fault aFault : this.info.portTypeOperation.faults()) {
/*     */         if (aFault.getName().equals(bindingFault.getName())) {
/*     */           if (portTypeFault != null) {
/*     */             error((Entity)bindingFault, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_NOT_UNIQUE(bindingFault.getName(), this.info.bindingOperation.getName()));
/*     */             continue;
/*     */           } 
/*     */           portTypeFault = aFault;
/*     */         } 
/*     */       } 
/*     */       if (portTypeFault == null)
/*     */         error((Entity)bindingFault, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_NOT_FOUND(bindingFault.getName(), this.info.bindingOperation.getName())); 
/*     */       SOAPFault soapFault = (SOAPFault)getExtensionOfType((TWSDLExtensible)bindingFault, SOAPFault.class);
/*     */       if (soapFault == null)
/*     */         if (this.options.isExtensionMode()) {
/*     */           warning((Entity)bindingFault, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_OUTPUT_MISSING_SOAP_FAULT(bindingFault.getName(), this.info.bindingOperation.getName()));
/*     */         } else {
/*     */           error((Entity)bindingFault, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_OUTPUT_MISSING_SOAP_FAULT(bindingFault.getName(), this.info.bindingOperation.getName()));
/*     */         }  
/*     */       Message faultMessage = portTypeFault.resolveMessage((AbstractDocument)this.info.document);
/*     */       if (faultMessage.getParts().isEmpty())
/*     */         error((Entity)faultMessage, ModelerMessages.WSDLMODELER_INVALID_BINDING_FAULT_EMPTY_MESSAGE(bindingFault.getName(), faultMessage.getName())); 
/*     */       if (!this.options.isExtensionMode() && soapFault != null && soapFault.getNamespace() != null)
/*     */         warning((Entity)soapFault, ModelerMessages.WSDLMODELER_WARNING_R_2716_R_2726("soapbind:fault", soapFault.getName())); 
/*     */       String faultNamespaceURI = (soapFault != null && soapFault.getNamespace() != null) ? soapFault.getNamespace() : portTypeFault.getMessage().getNamespaceURI();
/*     */       String faultName = faultMessage.getName();
/*     */       QName faultQName = new QName(faultNamespaceURI, faultName);
/*     */       if (faultNames.contains(faultQName)) {
/*     */         duplicateNames.add(faultQName);
/*     */         continue;
/*     */       } 
/*     */       faultNames.add(faultQName);
/*     */     } 
/*     */     return duplicateNames;
/*     */   }
/*     */   
/*     */   protected boolean validateBodyParts(BindingOperation operation) {
/*     */     boolean isRequestResponse = (this.info.portTypeOperation.getStyle() == OperationStyle.REQUEST_RESPONSE);
/*     */     List<MessagePart> inputParts = getMessageParts(getSOAPRequestBody(), getInputMessage(), true);
/*     */     if (!validateStyleAndPart(operation, inputParts))
/*     */       return false; 
/*     */     if (isRequestResponse) {
/*     */       List<MessagePart> outputParts = getMessageParts(getSOAPResponseBody(), getOutputMessage(), false);
/*     */       if (!validateStyleAndPart(operation, outputParts))
/*     */         return false; 
/*     */     } 
/*     */     return true;
/*     */   }
/*     */   
/*     */   private boolean validateStyleAndPart(BindingOperation operation, List<MessagePart> parts) {
/*     */     SOAPOperation soapOperation = (SOAPOperation)getExtensionOfType((TWSDLExtensible)operation, SOAPOperation.class);
/*     */     for (MessagePart part : parts) {
/*     */       if (part.getBindingExtensibilityElementKind() == 1 && !isStyleAndPartMatch(soapOperation, part))
/*     */         return false; 
/*     */     } 
/*     */     return true;
/*     */   }
/*     */   
/*     */   protected String getLiteralJavaMemberName(Fault fault) {
/*     */     QName memberName = fault.getElementName();
/*     */     String javaMemberName = fault.getJavaMemberName();
/*     */     if (javaMemberName == null)
/*     */       javaMemberName = memberName.getLocalPart(); 
/*     */     return javaMemberName;
/*     */   }
/*     */   
/*     */   protected List<MIMEContent> getMimeContents(TWSDLExtensible ext, Message message, String name) {
/*     */     for (MIMEPart mimePart : getMimeParts(ext)) {
/*     */       List<MIMEContent> mimeContents = getMimeContents(mimePart);
/*     */       for (MIMEContent mimeContent : mimeContents) {
/*     */         if (mimeContent.getPart().equals(name))
/*     */           return mimeContents; 
/*     */       } 
/*     */     } 
/*     */     return null;
/*     */   }
/*     */   
/*     */   protected String makePackageQualified(String s) {
/*     */     if (s.indexOf(".") != -1)
/*     */       return s; 
/*     */     if (this.options.defaultPackage != null && !this.options.defaultPackage.equals(""))
/*     */       return this.options.defaultPackage + "." + s; 
/*     */     return s;
/*     */   }
/*     */   
/*     */   protected String getUniqueName(Operation operation, boolean hasOverloadedOperations) {
/*     */     if (hasOverloadedOperations)
/*     */       return operation.getUniqueKey().replace(' ', '_'); 
/*     */     return operation.getName();
/*     */   }
/*     */   
/*     */   protected static QName getQNameOf(GloballyKnown entity) {
/*     */     return new QName(entity.getDefining().getTargetNamespaceURI(), entity.getName());
/*     */   }
/*     */   
/*     */   protected static TWSDLExtension getExtensionOfType(TWSDLExtensible extensible, Class type) {
/*     */     for (TWSDLExtension extension : extensible.extensions()) {
/*     */       if (extension.getClass().equals(type))
/*     */         return extension; 
/*     */     } 
/*     */     return null;
/*     */   }
/*     */   
/*     */   protected TWSDLExtension getAnyExtensionOfType(TWSDLExtensible extensible, Class type) {
/*     */     if (extensible == null)
/*     */       return null; 
/*     */     for (TWSDLExtension extension : extensible.extensions()) {
/*     */       if (extension.getClass().equals(type))
/*     */         return extension; 
/*     */       if (extension.getClass().equals(MIMEMultipartRelated.class) && (type.equals(SOAPBody.class) || type.equals(MIMEContent.class) || type.equals(MIMEPart.class)))
/*     */         for (MIMEPart part : ((MIMEMultipartRelated)extension).getParts()) {
/*     */           TWSDLExtension extn = getExtensionOfType((TWSDLExtensible)part, type);
/*     */           if (extn != null)
/*     */             return extn; 
/*     */         }  
/*     */     } 
/*     */     return null;
/*     */   }
/*     */   
/*     */   protected static Message findMessage(QName messageName, WSDLDocument document) {
/*     */     Message message = null;
/*     */     try {
/*     */       message = (Message)document.find(Kinds.MESSAGE, messageName);
/*     */     } catch (NoSuchEntityException noSuchEntityException) {}
/*     */     return message;
/*     */   }
/*     */   
/*     */   protected static boolean tokenListContains(String tokenList, String target) {
/*     */     if (tokenList == null)
/*     */       return false; 
/*     */     StringTokenizer tokenizer = new StringTokenizer(tokenList, " ");
/*     */     while (tokenizer.hasMoreTokens()) {
/*     */       String s = tokenizer.nextToken();
/*     */       if (target.equals(s))
/*     */         return true; 
/*     */     } 
/*     */     return false;
/*     */   }
/*     */   
/*     */   protected String getUniqueClassName(String className) {
/*     */     int cnt = 2;
/*     */     String uniqueName = className;
/*     */     while (this.reqResNames.contains(uniqueName.toLowerCase(Locale.ENGLISH))) {
/*     */       uniqueName = className + cnt;
/*     */       cnt++;
/*     */     } 
/*     */     this.reqResNames.add(uniqueName.toLowerCase(Locale.ENGLISH));
/*     */     return uniqueName;
/*     */   }
/*     */   
/*     */   protected boolean isConflictingClassName(String name) {
/*     */     if (this._conflictingClassNames == null)
/*     */       return false; 
/*     */     return this._conflictingClassNames.contains(name);
/*     */   }
/*     */   
/*     */   protected boolean isConflictingServiceClassName(String name) {
/*     */     return isConflictingClassName(name);
/*     */   }
/*     */   
/*     */   protected boolean isConflictingStubClassName(String name) {
/*     */     return isConflictingClassName(name);
/*     */   }
/*     */   
/*     */   protected boolean isConflictingTieClassName(String name) {
/*     */     return isConflictingClassName(name);
/*     */   }
/*     */   
/*     */   protected boolean isConflictingPortClassName(String name) {
/*     */     return isConflictingClassName(name);
/*     */   }
/*     */   
/*     */   protected boolean isConflictingExceptionClassName(String name) {
/*     */     return isConflictingClassName(name);
/*     */   }
/*     */   
/*     */   protected void warning(Entity entity, String message) {
/*     */     if (this.numPasses > 1)
/*     */       return; 
/*     */     if (entity == null) {
/*     */       this.errReceiver.warning(null, message);
/*     */     } else {
/*     */       this.errReceiver.warning(entity.getLocator(), message);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void error(Entity entity, String message) {
/*     */     if (entity == null) {
/*     */       this.errReceiver.error(null, message);
/*     */     } else {
/*     */       this.errReceiver.error(entity.getLocator(), message);
/*     */     } 
/*     */     throw new AbortException();
/*     */   }
/*     */   
/*     */   public WSDLModelerBase(WsimportOptions options, ErrorReceiver receiver, MetadataFinder forest) {
/* 712 */     this.numPasses = 0;
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
/*     */ 
/*     */ 
/*     */     
/* 752 */     this.reqResNames = new HashSet<>();
/*     */     this.options = options;
/*     */     this.errReceiver = new ErrorReceiverFilter((ErrorListener)receiver);
/*     */     this.forest = forest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ProcessSOAPOperationInfo
/*     */   {
/*     */     public ProcessSOAPOperationInfo(Port modelPort, Port port, Operation portTypeOperation, BindingOperation bindingOperation, SOAPBinding soapBinding, WSDLDocument document, boolean hasOverloadedOperations, Map headers) {
/* 765 */       this.modelPort = modelPort;
/* 766 */       this.port = port;
/* 767 */       this.portTypeOperation = portTypeOperation;
/* 768 */       this.bindingOperation = bindingOperation;
/* 769 */       this.soapBinding = soapBinding;
/* 770 */       this.document = document;
/* 771 */       this.hasOverloadedOperations = hasOverloadedOperations;
/* 772 */       this.headers = headers;
/*     */     }
/*     */ 
/*     */     
/*     */     public Port modelPort;
/*     */     
/*     */     public Port port;
/*     */     
/*     */     public Operation portTypeOperation;
/*     */     
/*     */     public BindingOperation bindingOperation;
/*     */     public SOAPBinding soapBinding;
/*     */     public WSDLDocument document;
/*     */     public boolean hasOverloadedOperations;
/*     */     public Map headers;
/*     */     public Operation operation;
/*     */   }
/*     */   
/* 790 */   protected static final LocatorImpl NULL_LOCATOR = new LocatorImpl();
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\modeler\wsdl\WSDLModelerBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */