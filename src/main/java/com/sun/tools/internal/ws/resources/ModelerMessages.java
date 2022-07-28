/*      */ package com.sun.tools.internal.ws.resources;
/*      */ 
/*      */ import com.sun.istack.internal.localization.Localizable;
/*      */ import com.sun.istack.internal.localization.LocalizableMessageFactory;
/*      */ import com.sun.istack.internal.localization.Localizer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ModelerMessages
/*      */ {
/*   39 */   private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.tools.internal.ws.resources.modeler");
/*   40 */   private static final Localizer localizer = new Localizer();
/*      */   
/*      */   public static Localizable localizableMIMEMODELER_INVALID_MIME_CONTENT_INVALID_SCHEMA_TYPE(Object arg0, Object arg1) {
/*   43 */     return messageFactory.getMessage("mimemodeler.invalidMimeContent.invalidSchemaType", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String MIMEMODELER_INVALID_MIME_CONTENT_INVALID_SCHEMA_TYPE(Object arg0, Object arg1) {
/*   51 */     return localizer.localize(localizableMIMEMODELER_INVALID_MIME_CONTENT_INVALID_SCHEMA_TYPE(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_PARAMETERORDER_PARAMETER(Object arg0, Object arg1) {
/*   55 */     return messageFactory.getMessage("wsdlmodeler.invalid.parameterorder.parameter", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_PARAMETERORDER_PARAMETER(Object arg0, Object arg1) {
/*   63 */     return localizer.localize(localizableWSDLMODELER_INVALID_PARAMETERORDER_PARAMETER(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_FAULT_NO_SOAP_FAULT_NAME(Object arg0, Object arg1) {
/*   67 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingFault.noSoapFaultName", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_FAULT_NO_SOAP_FAULT_NAME(Object arg0, Object arg1) {
/*   75 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_FAULT_NO_SOAP_FAULT_NAME(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_NONCONFORMING_WSDL_IMPORT() {
/*   79 */     return messageFactory.getMessage("wsdlmodeler.warning.nonconforming.wsdl.import", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_NONCONFORMING_WSDL_IMPORT() {
/*   87 */     return localizer.localize(localizableWSDLMODELER_WARNING_NONCONFORMING_WSDL_IMPORT());
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_OPERATION_OUTPUT_SOAP_BODY_MISSING_NAMESPACE(Object arg0) {
/*   91 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingOperation.outputSoapBody.missingNamespace", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_OPERATION_OUTPUT_SOAP_BODY_MISSING_NAMESPACE(Object arg0) {
/*   99 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_OPERATION_OUTPUT_SOAP_BODY_MISSING_NAMESPACE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_OPERATION_FAULT_NOT_LITERAL(Object arg0, Object arg1) {
/*  103 */     return messageFactory.getMessage("wsdlmodeler.invalid.operation.fault.notLiteral", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_OPERATION_FAULT_NOT_LITERAL(Object arg0, Object arg1) {
/*  111 */     return localizer.localize(localizableWSDLMODELER_INVALID_OPERATION_FAULT_NOT_LITERAL(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_OPERATION_INPUT_MISSING_SOAP_BODY(Object arg0) {
/*  115 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingOperation.inputMissingSoapBody", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_OPERATION_INPUT_MISSING_SOAP_BODY(Object arg0) {
/*  123 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_OPERATION_INPUT_MISSING_SOAP_BODY(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_SOAP_BINDING_NON_HTTP_TRANSPORT(Object arg0) {
/*  127 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringSOAPBinding.nonHTTPTransport", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_SOAP_BINDING_NON_HTTP_TRANSPORT(Object arg0) {
/*  135 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_SOAP_BINDING_NON_HTTP_TRANSPORT(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_OPERATION_NOT_FOUND(Object arg0, Object arg1) {
/*  139 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingOperation.notFound", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_OPERATION_NOT_FOUND(Object arg0, Object arg1) {
/*  147 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_OPERATION_NOT_FOUND(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_UNSUPPORTED_BINDING_MIME() {
/*  151 */     return messageFactory.getMessage("wsdlmodeler.unsupportedBinding.mime", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_UNSUPPORTED_BINDING_MIME() {
/*  159 */     return localizer.localize(localizableWSDLMODELER_UNSUPPORTED_BINDING_MIME());
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_HEADER_FAULT_NO_ELEMENT_ATTRIBUTE(Object arg0, Object arg1, Object arg2) {
/*  163 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringHeaderFault.noElementAttribute", new Object[] { arg0, arg1, arg2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_HEADER_FAULT_NO_ELEMENT_ATTRIBUTE(Object arg0, Object arg1, Object arg2) {
/*  171 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_HEADER_FAULT_NO_ELEMENT_ATTRIBUTE(arg0, arg1, arg2));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_NON_WRAPPER_STYLE(Object arg0, Object arg1, Object arg2) {
/*  175 */     return messageFactory.getMessage("wsdlmodeler.invalid.operation.javaReservedWordNotAllowed.nonWrapperStyle", new Object[] { arg0, arg1, arg2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_NON_WRAPPER_STYLE(Object arg0, Object arg1, Object arg2) {
/*  183 */     return localizer.localize(localizableWSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_NON_WRAPPER_STYLE(arg0, arg1, arg2));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_HEADERFAULT_NOT_LITERAL(Object arg0, Object arg1) {
/*  187 */     return messageFactory.getMessage("wsdlmodeler.invalid.headerfault.notLiteral", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_HEADERFAULT_NOT_LITERAL(Object arg0, Object arg1) {
/*  195 */     return localizer.localize(localizableWSDLMODELER_INVALID_HEADERFAULT_NOT_LITERAL(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableMIMEMODELER_INVALID_MIME_CONTENT_DIFFERENT_PART() {
/*  199 */     return messageFactory.getMessage("mimemodeler.invalidMimeContent.differentPart", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String MIMEMODELER_INVALID_MIME_CONTENT_DIFFERENT_PART() {
/*  207 */     return localizer.localize(localizableMIMEMODELER_INVALID_MIME_CONTENT_DIFFERENT_PART());
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_ERROR_PART_NOT_FOUND(Object arg0, Object arg1) {
/*  211 */     return messageFactory.getMessage("wsdlmodeler.error.partNotFound", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_ERROR_PART_NOT_FOUND(Object arg0, Object arg1) {
/*  219 */     return localizer.localize(localizableWSDLMODELER_ERROR_PART_NOT_FOUND(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_HEADER_MESSAGE_PART_MUST_HAVE_ELEMENT_DESCRIPTOR(Object arg0, Object arg1) {
/*  223 */     return messageFactory.getMessage("wsdlmodeler.invalid.header.message.partMustHaveElementDescriptor", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_HEADER_MESSAGE_PART_MUST_HAVE_ELEMENT_DESCRIPTOR(Object arg0, Object arg1) {
/*  231 */     return localizer.localize(localizableWSDLMODELER_INVALID_HEADER_MESSAGE_PART_MUST_HAVE_ELEMENT_DESCRIPTOR(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_OPERATION_NAME(Object arg0) {
/*  235 */     return messageFactory.getMessage("wsdlmodeler.invalid.operation.javaReservedWordNotAllowed.operationName", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_OPERATION_NAME(Object arg0) {
/*  243 */     return localizer.localize(localizableWSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_OPERATION_NAME(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_FAULT_OUTPUT_MISSING_SOAP_FAULT(Object arg0, Object arg1) {
/*  247 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingFault.outputMissingSoapFault", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_FAULT_OUTPUT_MISSING_SOAP_FAULT(Object arg0, Object arg1) {
/*  255 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_FAULT_OUTPUT_MISSING_SOAP_FAULT(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_ELEMENT_MESSAGE_PART(Object arg0) {
/*  259 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.cannotHandleElementMessagePart", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_ELEMENT_MESSAGE_PART(Object arg0) {
/*  267 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_ELEMENT_MESSAGE_PART(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODLER_WARNING_OPERATION_USE() {
/*  271 */     return messageFactory.getMessage("wsdlmodler.warning.operation.use", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODLER_WARNING_OPERATION_USE() {
/*  279 */     return localizer.localize(localizableWSDLMODLER_WARNING_OPERATION_USE());
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_NON_SOAP_PORT(Object arg0) {
/*  283 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringNonSOAPPort", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_NON_SOAP_PORT(Object arg0) {
/*  291 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_NON_SOAP_PORT(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_FAULT_MESSAGE_HAS_MORE_THAN_ONE_PART(Object arg0, Object arg1) {
/*  295 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingFault.messageHasMoreThanOnePart", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_FAULT_MESSAGE_HAS_MORE_THAN_ONE_PART(Object arg0, Object arg1) {
/*  303 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_FAULT_MESSAGE_HAS_MORE_THAN_ONE_PART(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_NO_SERVICE_DEFINITIONS_FOUND() {
/*  307 */     return messageFactory.getMessage("wsdlmodeler.warning.noServiceDefinitionsFound", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_NO_SERVICE_DEFINITIONS_FOUND() {
/*  315 */     return localizer.localize(localizableWSDLMODELER_WARNING_NO_SERVICE_DEFINITIONS_FOUND());
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_FAULT_CANT_RESOLVE_MESSAGE(Object arg0, Object arg1) {
/*  319 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringFault.cant.resolve.message", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_FAULT_CANT_RESOLVE_MESSAGE(Object arg0, Object arg1) {
/*  327 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_FAULT_CANT_RESOLVE_MESSAGE(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_JAXB_JAVATYPE_NOTFOUND(Object arg0, Object arg1) {
/*  331 */     return messageFactory.getMessage("wsdlmodeler.jaxb.javatype.notfound", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_JAXB_JAVATYPE_NOTFOUND(Object arg0, Object arg1) {
/*  339 */     return localizer.localize(localizableWSDLMODELER_JAXB_JAVATYPE_NOTFOUND(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_PORT_SOAP_BINDING_MIXED_STYLE(Object arg0) {
/*  343 */     return messageFactory.getMessage("wsdlmodeler.warning.port.SOAPBinding.mixedStyle", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_PORT_SOAP_BINDING_MIXED_STYLE(Object arg0) {
/*  351 */     return localizer.localize(localizableWSDLMODELER_WARNING_PORT_SOAP_BINDING_MIXED_STYLE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_DOCLITOPERATION(Object arg0) {
/*  355 */     return messageFactory.getMessage("wsdlmodeler.invalid.doclitoperation", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_DOCLITOPERATION(Object arg0) {
/*  363 */     return localizer.localize(localizableWSDLMODELER_INVALID_DOCLITOPERATION(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableMODELER_NESTED_MODEL_ERROR(Object arg0) {
/*  367 */     return messageFactory.getMessage("modeler.nestedModelError", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String MODELER_NESTED_MODEL_ERROR(Object arg0) {
/*  375 */     return localizer.localize(localizableMODELER_NESTED_MODEL_ERROR(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_DUPLICATE_FAULT_SOAP_NAME(Object arg0, Object arg1, Object arg2) {
/*  379 */     return messageFactory.getMessage("wsdlmodeler.duplicate.fault.soap.name", new Object[] { arg0, arg1, arg2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_DUPLICATE_FAULT_SOAP_NAME(Object arg0, Object arg1, Object arg2) {
/*  387 */     return localizer.localize(localizableWSDLMODELER_DUPLICATE_FAULT_SOAP_NAME(arg0, arg1, arg2));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_FAULT_WRONG_SOAP_FAULT_NAME(Object arg0, Object arg1, Object arg2) {
/*  391 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingFault.wrongSoapFaultName", new Object[] { arg0, arg1, arg2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_FAULT_WRONG_SOAP_FAULT_NAME(Object arg0, Object arg1, Object arg2) {
/*  399 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_FAULT_WRONG_SOAP_FAULT_NAME(arg0, arg1, arg2));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_NOT_LITERAL(Object arg0) {
/*  403 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.notLiteral", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_NOT_LITERAL(Object arg0) {
/*  411 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_NOT_LITERAL(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_DOCUMENT_STYLE(Object arg0) {
/*  415 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.cannotHandleDocumentStyle", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_DOCUMENT_STYLE(Object arg0) {
/*  423 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_DOCUMENT_STYLE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_FAULT_NOT_LITERAL(Object arg0, Object arg1) {
/*  427 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringFault.notLiteral", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_FAULT_NOT_LITERAL(Object arg0, Object arg1) {
/*  435 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_FAULT_NOT_LITERAL(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_RPCLITOPERATION(Object arg0) {
/*  439 */     return messageFactory.getMessage("wsdlmodeler.invalid.rpclitoperation", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_RPCLITOPERATION(Object arg0) {
/*  447 */     return localizer.localize(localizableWSDLMODELER_INVALID_RPCLITOPERATION(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_CUSTOM_NAME(Object arg0, Object arg1) {
/*  451 */     return messageFactory.getMessage("wsdlmodeler.invalid.operation.javaReservedWordNotAllowed.customName", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_CUSTOM_NAME(Object arg0, Object arg1) {
/*  459 */     return localizer.localize(localizableWSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_CUSTOM_NAME(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_MEMBER_SUBMISSION_ADDRESSING_USED(Object arg0, Object arg1) {
/*  463 */     return messageFactory.getMessage("wsdlmodeler.warning.memberSubmissionAddressingUsed", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_MEMBER_SUBMISSION_ADDRESSING_USED(Object arg0, Object arg1) {
/*  471 */     return localizer.localize(localizableWSDLMODELER_WARNING_MEMBER_SUBMISSION_ADDRESSING_USED(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_BINDING_OPERATION_MULTIPLE_PART_BINDING(Object arg0, Object arg1) {
/*  475 */     return messageFactory.getMessage("wsdlmodeler.warning.bindingOperation.multiplePartBinding", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_BINDING_OPERATION_MULTIPLE_PART_BINDING(Object arg0, Object arg1) {
/*  483 */     return localizer.localize(localizableWSDLMODELER_WARNING_BINDING_OPERATION_MULTIPLE_PART_BINDING(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_PORT_SOAP_BINDING_12(Object arg0) {
/*  487 */     return messageFactory.getMessage("wsdlmodeler.warning.port.SOAPBinding12", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_PORT_SOAP_BINDING_12(Object arg0) {
/*  495 */     return localizer.localize(localizableWSDLMODELER_WARNING_PORT_SOAP_BINDING_12(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_PORT_TYPE_FAULT_NOT_FOUND(Object arg0, Object arg1) {
/*  499 */     return messageFactory.getMessage("wsdlmodeler.invalid.portTypeFault.notFound", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_PORT_TYPE_FAULT_NOT_FOUND(Object arg0, Object arg1) {
/*  507 */     return localizer.localize(localizableWSDLMODELER_INVALID_PORT_TYPE_FAULT_NOT_FOUND(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableMIMEMODELER_INVALID_MIME_PART_NAME_NOT_ALLOWED(Object arg0) {
/*  511 */     return messageFactory.getMessage("mimemodeler.invalidMimePart.nameNotAllowed", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String MIMEMODELER_INVALID_MIME_PART_NAME_NOT_ALLOWED(Object arg0) {
/*  519 */     return localizer.localize(localizableMIMEMODELER_INVALID_MIME_PART_NAME_NOT_ALLOWED(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_MIME_PART_NOT_FOUND(Object arg0, Object arg1) {
/*  523 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringMimePart.notFound", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_MIME_PART_NOT_FOUND(Object arg0, Object arg1) {
/*  531 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_MIME_PART_NOT_FOUND(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_OPERATION_MORE_THAN_ONE_PART_IN_MESSAGE(Object arg0) {
/*  535 */     return messageFactory.getMessage("wsdlmodeler.warning.operation.MoreThanOnePartInMessage", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_OPERATION_MORE_THAN_ONE_PART_IN_MESSAGE(Object arg0) {
/*  543 */     return localizer.localize(localizableWSDLMODELER_WARNING_OPERATION_MORE_THAN_ONE_PART_IN_MESSAGE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_NON_WRAPPER_STYLE(Object arg0, Object arg1, Object arg2) {
/*  547 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.javaReservedWordNotAllowed.nonWrapperStyle", new Object[] { arg0, arg1, arg2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_NON_WRAPPER_STYLE(Object arg0, Object arg1, Object arg2) {
/*  555 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_NON_WRAPPER_STYLE(arg0, arg1, arg2));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_FAULT_CANT_RESOLVE_MESSAGE(Object arg0, Object arg1) {
/*  559 */     return messageFactory.getMessage("wsdlmodeler.invalid.fault.cant.resolve.message", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_FAULT_CANT_RESOLVE_MESSAGE(Object arg0, Object arg1) {
/*  567 */     return localizer.localize(localizableWSDLMODELER_INVALID_FAULT_CANT_RESOLVE_MESSAGE(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_FAULT_EMPTY_MESSAGE(Object arg0, Object arg1) {
/*  571 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingFault.emptyMessage", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_FAULT_EMPTY_MESSAGE(Object arg0, Object arg1) {
/*  579 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_FAULT_EMPTY_MESSAGE(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_RPCLIT_UNKOWNSCHEMATYPE(Object arg0, Object arg1, Object arg2) {
/*  583 */     return messageFactory.getMessage("wsdlmodeler.rpclit.unkownschematype", new Object[] { arg0, arg1, arg2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_RPCLIT_UNKOWNSCHEMATYPE(Object arg0, Object arg1, Object arg2) {
/*  591 */     return localizer.localize(localizableWSDLMODELER_RPCLIT_UNKOWNSCHEMATYPE(arg0, arg1, arg2));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_BODY_PARTS_ATTRIBUTE(Object arg0) {
/*  595 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.cannotHandleBodyPartsAttribute", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_BODY_PARTS_ATTRIBUTE(Object arg0) {
/*  603 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_BODY_PARTS_ATTRIBUTE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_NON_UNIQUE_BODY_ERROR(Object arg0, Object arg1, Object arg2, Object arg3) {
/*  607 */     return messageFactory.getMessage("wsdlmodeler.nonUnique.body.error", new Object[] { arg0, arg1, arg2, arg3 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_NON_UNIQUE_BODY_ERROR(Object arg0, Object arg1, Object arg2, Object arg3) {
/*  615 */     return localizer.localize(localizableWSDLMODELER_NON_UNIQUE_BODY_ERROR(arg0, arg1, arg2, arg3));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_SOAP_BINDING_MIXED_STYLE(Object arg0) {
/*  619 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringSOAPBinding.mixedStyle", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_SOAP_BINDING_MIXED_STYLE(Object arg0) {
/*  627 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_SOAP_BINDING_MIXED_STYLE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableMIMEMODELER_INVALID_MIME_CONTENT_MISSING_TYPE_ATTRIBUTE(Object arg0) {
/*  631 */     return messageFactory.getMessage("mimemodeler.invalidMimeContent.missingTypeAttribute", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String MIMEMODELER_INVALID_MIME_CONTENT_MISSING_TYPE_ATTRIBUTE(Object arg0) {
/*  639 */     return localizer.localize(localizableMIMEMODELER_INVALID_MIME_CONTENT_MISSING_TYPE_ATTRIBUTE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_EMPTY_INPUT_MESSAGE(Object arg0) {
/*  643 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.cannotHandleEmptyInputMessage", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_EMPTY_INPUT_MESSAGE(Object arg0) {
/*  651 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_EMPTY_INPUT_MESSAGE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_NO_PORTS_IN_SERVICE(Object arg0) {
/*  655 */     return messageFactory.getMessage("wsdlmodeler.warning.noPortsInService", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_NO_PORTS_IN_SERVICE(Object arg0) {
/*  663 */     return localizer.localize(localizableWSDLMODELER_WARNING_NO_PORTS_IN_SERVICE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_PARAMETER_ORDER_TOO_MANY_UNMENTIONED_PARTS(Object arg0) {
/*  667 */     return messageFactory.getMessage("wsdlmodeler.invalid.parameterOrder.tooManyUnmentionedParts", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_PARAMETER_ORDER_TOO_MANY_UNMENTIONED_PARTS(Object arg0) {
/*  675 */     return localizer.localize(localizableWSDLMODELER_INVALID_PARAMETER_ORDER_TOO_MANY_UNMENTIONED_PARTS(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_OPERATION_INPUT_SOAP_BODY_MISSING_NAMESPACE(Object arg0) {
/*  679 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingOperation.inputSoapBody.missingNamespace", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_OPERATION_INPUT_SOAP_BODY_MISSING_NAMESPACE(Object arg0) {
/*  687 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_OPERATION_INPUT_SOAP_BODY_MISSING_NAMESPACE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_HEADER(Object arg0, Object arg1) {
/*  691 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringHeader", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_HEADER(Object arg0, Object arg1) {
/*  699 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_HEADER(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_RESPONSEBEAN_NOTFOUND(Object arg0) {
/*  703 */     return messageFactory.getMessage("wsdlmodeler.responsebean.notfound", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_RESPONSEBEAN_NOTFOUND(Object arg0) {
/*  711 */     return localizer.localize(localizableWSDLMODELER_RESPONSEBEAN_NOTFOUND(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_20_RPCENC_NOT_SUPPORTED() {
/*  715 */     return messageFactory.getMessage("wsdlmodeler20.rpcenc.not.supported", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_20_RPCENC_NOT_SUPPORTED() {
/*  723 */     return localizer.localize(localizableWSDLMODELER_20_RPCENC_NOT_SUPPORTED());
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_PART_NOT_FOUND(Object arg0, Object arg1) {
/*  727 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.partNotFound", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_PART_NOT_FOUND(Object arg0, Object arg1) {
/*  735 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_PART_NOT_FOUND(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_MESSAGE_PART_MUST_HAVE_ELEMENT_DESCRIPTOR(Object arg0, Object arg1) {
/*  739 */     return messageFactory.getMessage("wsdlmodeler.invalid.message.partMustHaveElementDescriptor", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_MESSAGE_PART_MUST_HAVE_ELEMENT_DESCRIPTOR(Object arg0, Object arg1) {
/*  747 */     return localizer.localize(localizableWSDLMODELER_INVALID_MESSAGE_PART_MUST_HAVE_ELEMENT_DESCRIPTOR(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_ERROR_PARTS_NOT_FOUND(Object arg0, Object arg1) {
/*  751 */     return messageFactory.getMessage("wsdlmodeler.error.partsNotFound", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_ERROR_PARTS_NOT_FOUND(Object arg0, Object arg1) {
/*  759 */     return localizer.localize(localizableWSDLMODELER_ERROR_PARTS_NOT_FOUND(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_FAULT_NOT_ENCODED(Object arg0, Object arg1) {
/*  763 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringFault.notEncoded", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_FAULT_NOT_ENCODED(Object arg0, Object arg1) {
/*  771 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_FAULT_NOT_ENCODED(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_NOT_SUPPORTED_STYLE(Object arg0) {
/*  775 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.notSupportedStyle", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_NOT_SUPPORTED_STYLE(Object arg0) {
/*  783 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_NOT_SUPPORTED_STYLE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_OPERATION_MULTIPLE_PART_BINDING(Object arg0, Object arg1) {
/*  787 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingOperation.multiplePartBinding", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_OPERATION_MULTIPLE_PART_BINDING(Object arg0, Object arg1) {
/*  795 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_OPERATION_MULTIPLE_PART_BINDING(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_OPERATION_MULTIPLE_MATCHING_OPERATIONS(Object arg0, Object arg1) {
/*  799 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingOperation.multipleMatchingOperations", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_OPERATION_MULTIPLE_MATCHING_OPERATIONS(Object arg0, Object arg1) {
/*  807 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_OPERATION_MULTIPLE_MATCHING_OPERATIONS(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_HEADER_CANT_RESOLVE_MESSAGE(Object arg0, Object arg1) {
/*  811 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringHeader.cant.resolve.message", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_HEADER_CANT_RESOLVE_MESSAGE(Object arg0, Object arg1) {
/*  819 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_HEADER_CANT_RESOLVE_MESSAGE(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_CUSTOMIZED_OPERATION_NAME(Object arg0, Object arg1) {
/*  823 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.javaReservedWordNotAllowed.customizedOperationName", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_CUSTOMIZED_OPERATION_NAME(Object arg0, Object arg1) {
/*  831 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_CUSTOMIZED_OPERATION_NAME(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_HEADER_NOT_LITERAL(Object arg0, Object arg1) {
/*  835 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringHeader.notLiteral", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_HEADER_NOT_LITERAL(Object arg0, Object arg1) {
/*  843 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_HEADER_NOT_LITERAL(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_OPERATION_INPUT_HEADER_MISSING_NAMESPACE(Object arg0, Object arg1) {
/*  847 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingOperation.inputHeader.missingNamespace", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_OPERATION_INPUT_HEADER_MISSING_NAMESPACE(Object arg0, Object arg1) {
/*  855 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_OPERATION_INPUT_HEADER_MISSING_NAMESPACE(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_OPERATION_MISSING_INPUT_NAME(Object arg0) {
/*  859 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingOperation.missingInputName", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_OPERATION_MISSING_INPUT_NAME(Object arg0) {
/*  867 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_OPERATION_MISSING_INPUT_NAME(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_NON_SOAP_PORT_NO_ADDRESS(Object arg0) {
/*  871 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringNonSOAPPort.noAddress", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_NON_SOAP_PORT_NO_ADDRESS(Object arg0) {
/*  879 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_NON_SOAP_PORT_NO_ADDRESS(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_RESULT_IS_IN_OUT_PARAMETER(Object arg0) {
/*  883 */     return messageFactory.getMessage("wsdlmodeler.resultIsInOutParameter", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_RESULT_IS_IN_OUT_PARAMETER(Object arg0) {
/*  891 */     return localizer.localize(localizableWSDLMODELER_RESULT_IS_IN_OUT_PARAMETER(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_HEADER_NOT_FOUND(Object arg0, Object arg1) {
/*  895 */     return messageFactory.getMessage("wsdlmodeler.invalid.header.notFound", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_HEADER_NOT_FOUND(Object arg0, Object arg1) {
/*  903 */     return localizer.localize(localizableWSDLMODELER_INVALID_HEADER_NOT_FOUND(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableMIMEMODELER_ELEMENT_PART_INVALID_ELEMENT_MIME_TYPE(Object arg0, Object arg1) {
/*  907 */     return messageFactory.getMessage("mimemodeler.elementPart.invalidElementMimeType", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String MIMEMODELER_ELEMENT_PART_INVALID_ELEMENT_MIME_TYPE(Object arg0, Object arg1) {
/*  915 */     return localizer.localize(localizableMIMEMODELER_ELEMENT_PART_INVALID_ELEMENT_MIME_TYPE(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_HEADER_NOT_LITERAL(Object arg0, Object arg1) {
/*  919 */     return messageFactory.getMessage("wsdlmodeler.invalid.header.notLiteral", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_HEADER_NOT_LITERAL(Object arg0, Object arg1) {
/*  927 */     return localizer.localize(localizableWSDLMODELER_INVALID_HEADER_NOT_LITERAL(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableMIMEMODELER_INVALID_MIME_CONTENT_MESAGE_PART_ELEMENT_KIND(Object arg0) {
/*  931 */     return messageFactory.getMessage("mimemodeler.invalidMimeContent.mesagePartElementKind", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String MIMEMODELER_INVALID_MIME_CONTENT_MESAGE_PART_ELEMENT_KIND(Object arg0) {
/*  939 */     return localizer.localize(localizableMIMEMODELER_INVALID_MIME_CONTENT_MESAGE_PART_ELEMENT_KIND(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_NOT_ENCODED(Object arg0) {
/*  943 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.notEncoded", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_NOT_ENCODED(Object arg0) {
/*  951 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_NOT_ENCODED(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_NONCONFORMING_WSDL_TYPES() {
/*  955 */     return messageFactory.getMessage("wsdlmodeler.warning.nonconforming.wsdl.types", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_NONCONFORMING_WSDL_TYPES() {
/*  963 */     return localizer.localize(localizableWSDLMODELER_WARNING_NONCONFORMING_WSDL_TYPES());
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_MORE_THAN_ONE_PART_IN_INPUT_MESSAGE(Object arg0) {
/*  967 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.cannotHandleMoreThanOnePartInInputMessage", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_MORE_THAN_ONE_PART_IN_INPUT_MESSAGE(Object arg0) {
/*  975 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_MORE_THAN_ONE_PART_IN_INPUT_MESSAGE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_EMPTY_OUTPUT_MESSAGE(Object arg0) {
/*  979 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.cannotHandleEmptyOutputMessage", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_EMPTY_OUTPUT_MESSAGE(Object arg0) {
/*  987 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_EMPTY_OUTPUT_MESSAGE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_R_2716_R_2726(Object arg0, Object arg1) {
/*  991 */     return messageFactory.getMessage("wsdlmodeler.warning.r2716r2726", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_R_2716_R_2726(Object arg0, Object arg1) {
/*  999 */     return localizer.localize(localizableWSDLMODELER_WARNING_R_2716_R_2726(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_IGNORING_MEMBER_SUBMISSION_ADDRESSING(Object arg0, Object arg1) {
/* 1003 */     return messageFactory.getMessage("wsdlmodeler.invalid.ignoringMemberSubmissionAddressing", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_IGNORING_MEMBER_SUBMISSION_ADDRESSING(Object arg0, Object arg1) {
/* 1011 */     return localizer.localize(localizableWSDLMODELER_INVALID_IGNORING_MEMBER_SUBMISSION_ADDRESSING(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_NO_SOAP_ADDRESS(Object arg0) {
/* 1015 */     return messageFactory.getMessage("wsdlmodeler.warning.noSOAPAddress", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_NO_SOAP_ADDRESS(Object arg0) {
/* 1023 */     return localizer.localize(localizableWSDLMODELER_WARNING_NO_SOAP_ADDRESS(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_FAULTS(Object arg0) {
/* 1027 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringFaults", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_FAULTS(Object arg0) {
/* 1035 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_FAULTS(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_FAULT_MISSING_NAME(Object arg0, Object arg1) {
/* 1039 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingFault.missingName", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_FAULT_MISSING_NAME(Object arg0, Object arg1) {
/* 1047 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_FAULT_MISSING_NAME(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableMIMEMODELER_WARNING_IGNORINGINVALID_HEADER_PART_NOT_DECLARED_IN_ROOT_PART(Object arg0) {
/* 1051 */     return messageFactory.getMessage("mimemodeler.warning.IgnoringinvalidHeaderPart.notDeclaredInRootPart", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String MIMEMODELER_WARNING_IGNORINGINVALID_HEADER_PART_NOT_DECLARED_IN_ROOT_PART(Object arg0) {
/* 1059 */     return localizer.localize(localizableMIMEMODELER_WARNING_IGNORINGINVALID_HEADER_PART_NOT_DECLARED_IN_ROOT_PART(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableMIMEMODELER_INVALID_MIME_CONTENT_ERROR_LOADING_JAVA_CLASS() {
/* 1063 */     return messageFactory.getMessage("mimemodeler.invalidMimeContent.errorLoadingJavaClass", new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String MIMEMODELER_INVALID_MIME_CONTENT_ERROR_LOADING_JAVA_CLASS() {
/* 1071 */     return localizer.localize(localizableMIMEMODELER_INVALID_MIME_CONTENT_ERROR_LOADING_JAVA_CLASS());
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_OPERATION_NOT_IN_PORT_TYPE(Object arg0, Object arg1) {
/* 1075 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingOperation.notInPortType", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_OPERATION_NOT_IN_PORT_TYPE(Object arg0, Object arg1) {
/* 1083 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_OPERATION_NOT_IN_PORT_TYPE(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CONFLICT_STYLE_IN_WSI_MODE(Object arg0) {
/* 1087 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.conflictStyleInWSIMode", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_CONFLICT_STYLE_IN_WSI_MODE(Object arg0) {
/* 1095 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CONFLICT_STYLE_IN_WSI_MODE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableMIMEMODELER_INVALID_MIME_CONTENT_MISSING_PART_ATTRIBUTE(Object arg0) {
/* 1099 */     return messageFactory.getMessage("mimemodeler.invalidMimeContent.missingPartAttribute", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String MIMEMODELER_INVALID_MIME_CONTENT_MISSING_PART_ATTRIBUTE(Object arg0) {
/* 1107 */     return localizer.localize(localizableMIMEMODELER_INVALID_MIME_CONTENT_MISSING_PART_ATTRIBUTE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_SEARCH_SCHEMA_UNRECOGNIZED_TYPES(Object arg0) {
/* 1111 */     return messageFactory.getMessage("wsdlmodeler.warning.searchSchema.unrecognizedTypes", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_SEARCH_SCHEMA_UNRECOGNIZED_TYPES(Object arg0) {
/* 1119 */     return localizer.localize(localizableWSDLMODELER_WARNING_SEARCH_SCHEMA_UNRECOGNIZED_TYPES(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_CUSTOMIZED_OPERATION_NAME(Object arg0, Object arg1) {
/* 1123 */     return messageFactory.getMessage("wsdlmodeler.invalid.operation.javaReservedWordNotAllowed.customizedOperationName", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_CUSTOMIZED_OPERATION_NAME(Object arg0, Object arg1) {
/* 1131 */     return localizer.localize(localizableWSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_CUSTOMIZED_OPERATION_NAME(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_HEADER_CANT_RESOLVE_MESSAGE(Object arg0, Object arg1) {
/* 1135 */     return messageFactory.getMessage("wsdlmodeler.invalid.header.cant.resolve.message", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_HEADER_CANT_RESOLVE_MESSAGE(Object arg0, Object arg1) {
/* 1143 */     return localizer.localize(localizableWSDLMODELER_INVALID_HEADER_CANT_RESOLVE_MESSAGE(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_FAULT_MISSING_NAMESPACE(Object arg0, Object arg1) {
/* 1147 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingFault.missingNamespace", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_FAULT_MISSING_NAMESPACE(Object arg0, Object arg1) {
/* 1155 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_FAULT_MISSING_NAMESPACE(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableMIMEMODELER_INVALID_MIME_PART_MORE_THAN_ONE_SOAP_BODY(Object arg0) {
/* 1159 */     return messageFactory.getMessage("mimemodeler.invalidMimePart.moreThanOneSOAPBody", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String MIMEMODELER_INVALID_MIME_PART_MORE_THAN_ONE_SOAP_BODY(Object arg0) {
/* 1167 */     return localizer.localize(localizableMIMEMODELER_INVALID_MIME_PART_MORE_THAN_ONE_SOAP_BODY(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_HEADER_INCONSISTENT_DEFINITION(Object arg0, Object arg1) {
/* 1171 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringHeader.inconsistentDefinition", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_HEADER_INCONSISTENT_DEFINITION(Object arg0, Object arg1) {
/* 1179 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_HEADER_INCONSISTENT_DEFINITION(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_FAULT_NOT_FOUND(Object arg0, Object arg1) {
/* 1183 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingFault.notFound", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_FAULT_NOT_FOUND(Object arg0, Object arg1) {
/* 1191 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_FAULT_NOT_FOUND(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_CUSTOM_NAME(Object arg0, Object arg1) {
/* 1195 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.javaReservedWordNotAllowed.customName", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_CUSTOM_NAME(Object arg0, Object arg1) {
/* 1203 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_CUSTOM_NAME(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_UNRECOGNIZED_SCHEMA_EXTENSION(Object arg0) {
/* 1207 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringUnrecognizedSchemaExtension", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_UNRECOGNIZED_SCHEMA_EXTENSION(Object arg0) {
/* 1215 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_UNRECOGNIZED_SCHEMA_EXTENSION(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_HEADER_FAULT_NOT_FOUND(Object arg0, Object arg1, Object arg2) {
/* 1219 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringHeaderFault.notFound", new Object[] { arg0, arg1, arg2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_HEADER_FAULT_NOT_FOUND(Object arg0, Object arg1, Object arg2) {
/* 1227 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_HEADER_FAULT_NOT_FOUND(arg0, arg1, arg2));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_NON_UNIQUE_BODY_WARNING(Object arg0, Object arg1, Object arg2, Object arg3) {
/* 1231 */     return messageFactory.getMessage("wsdlmodeler.nonUnique.body.warning", new Object[] { arg0, arg1, arg2, arg3 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_NON_UNIQUE_BODY_WARNING(Object arg0, Object arg1, Object arg2, Object arg3) {
/* 1239 */     return localizer.localize(localizableWSDLMODELER_NON_UNIQUE_BODY_WARNING(arg0, arg1, arg2, arg3));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_WRAPPER_STYLE(Object arg0, Object arg1, Object arg2) {
/* 1243 */     return messageFactory.getMessage("wsdlmodeler.invalid.operation.javaReservedWordNotAllowed.wrapperStyle", new Object[] { arg0, arg1, arg2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_WRAPPER_STYLE(Object arg0, Object arg1, Object arg2) {
/* 1251 */     return localizer.localize(localizableWSDLMODELER_INVALID_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_WRAPPER_STYLE(arg0, arg1, arg2));
/*      */   }
/*      */   
/*      */   public static Localizable localizableMIMEMODELER_INVALID_MIME_CONTENT_UNKNOWN_SCHEMA_TYPE(Object arg0, Object arg1) {
/* 1255 */     return messageFactory.getMessage("mimemodeler.invalidMimeContent.unknownSchemaType", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String MIMEMODELER_INVALID_MIME_CONTENT_UNKNOWN_SCHEMA_TYPE(Object arg0, Object arg1) {
/* 1263 */     return localizer.localize(localizableMIMEMODELER_INVALID_MIME_CONTENT_UNKNOWN_SCHEMA_TYPE(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_R_2716(Object arg0, Object arg1) {
/* 1267 */     return messageFactory.getMessage("wsdlmodeler.warning.r2716", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_R_2716(Object arg0, Object arg1) {
/* 1275 */     return localizer.localize(localizableWSDLMODELER_WARNING_R_2716(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_HEADER_NOT_FOUND(Object arg0, Object arg1) {
/* 1279 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringHeader.notFound", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_HEADER_NOT_FOUND(Object arg0, Object arg1) {
/* 1287 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_HEADER_NOT_FOUND(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_TYPE_MESSAGE_PART(Object arg0) {
/* 1291 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.cannotHandleTypeMessagePart", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_TYPE_MESSAGE_PART(Object arg0) {
/* 1299 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_TYPE_MESSAGE_PART(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_PARAMETER_ORDER_INVALID_PARAMETER_ORDER(Object arg0) {
/* 1303 */     return messageFactory.getMessage("wsdlmodeler.invalid.parameterOrder.invalidParameterOrder", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_PARAMETER_ORDER_INVALID_PARAMETER_ORDER(Object arg0) {
/* 1311 */     return localizer.localize(localizableWSDLMODELER_INVALID_PARAMETER_ORDER_INVALID_PARAMETER_ORDER(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_OPERATION_MISSING_OUTPUT_NAME(Object arg0) {
/* 1315 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingOperation.missingOutputName", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_OPERATION_MISSING_OUTPUT_NAME(Object arg0) {
/* 1323 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_OPERATION_MISSING_OUTPUT_NAME(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_OPERATION(Object arg0) {
/* 1327 */     return messageFactory.getMessage("wsdlmodeler.invalidOperation", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_OPERATION(Object arg0) {
/* 1335 */     return localizer.localize(localizableWSDLMODELER_INVALID_OPERATION(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_OPERATION_OUTPUT_HEADER_MISSING_NAMESPACE(Object arg0, Object arg1) {
/* 1339 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingOperation.outputHeader.missingNamespace", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_OPERATION_OUTPUT_HEADER_MISSING_NAMESPACE(Object arg0, Object arg1) {
/* 1347 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_OPERATION_OUTPUT_HEADER_MISSING_NAMESPACE(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_HEADER_PART_FROM_BODY(Object arg0) {
/* 1351 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringHeader.partFromBody", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_HEADER_PART_FROM_BODY(Object arg0) {
/* 1359 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_HEADER_PART_FROM_BODY(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_OPERATION_NOT_SUPPORTED_STYLE(Object arg0, Object arg1) {
/* 1363 */     return messageFactory.getMessage("wsdlmodeler.invalid.operation.notSupportedStyle", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_OPERATION_NOT_SUPPORTED_STYLE(Object arg0, Object arg1) {
/* 1371 */     return localizer.localize(localizableWSDLMODELER_INVALID_OPERATION_NOT_SUPPORTED_STYLE(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_NOT_NC_NAME(Object arg0, Object arg1) {
/* 1375 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.notNCName", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_NOT_NC_NAME(Object arg0, Object arg1) {
/* 1383 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_NOT_NC_NAME(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_PARAMETER_DIFFERENT_TYPES(Object arg0, Object arg1) {
/* 1387 */     return messageFactory.getMessage("wsdlmodeler.invalid.parameter.differentTypes", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_PARAMETER_DIFFERENT_TYPES(Object arg0, Object arg1) {
/* 1395 */     return localizer.localize(localizableWSDLMODELER_INVALID_PARAMETER_DIFFERENT_TYPES(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_FAULT_DOCUMENT_OPERATION(Object arg0, Object arg1) {
/* 1399 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringFault.documentOperation", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_FAULT_DOCUMENT_OPERATION(Object arg0, Object arg1) {
/* 1407 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_FAULT_DOCUMENT_OPERATION(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_NONCONFORMING_WSDL_USE(Object arg0) {
/* 1411 */     return messageFactory.getMessage("wsdlmodeler.warning.nonconforming.wsdl.use", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_NONCONFORMING_WSDL_USE(Object arg0) {
/* 1419 */     return localizer.localize(localizableWSDLMODELER_WARNING_NONCONFORMING_WSDL_USE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_NON_SOAP_PORT(Object arg0) {
/* 1423 */     return messageFactory.getMessage("wsdlmodeler.warning.nonSOAPPort", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_NON_SOAP_PORT(Object arg0) {
/* 1431 */     return localizer.localize(localizableWSDLMODELER_WARNING_NON_SOAP_PORT(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_HEADERFAULT_MESSAGE_PART_MUST_HAVE_ELEMENT_DESCRIPTOR(Object arg0, Object arg1, Object arg2) {
/* 1435 */     return messageFactory.getMessage("wsdlmodeler.invalid.headerfault.message.partMustHaveElementDescriptor", new Object[] { arg0, arg1, arg2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_HEADERFAULT_MESSAGE_PART_MUST_HAVE_ELEMENT_DESCRIPTOR(Object arg0, Object arg1, Object arg2) {
/* 1443 */     return localizer.localize(localizableWSDLMODELER_INVALID_HEADERFAULT_MESSAGE_PART_MUST_HAVE_ELEMENT_DESCRIPTOR(arg0, arg1, arg2));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_STATE_MODELING_OPERATION(Object arg0) {
/* 1447 */     return messageFactory.getMessage("wsdlmodeler.invalidState.modelingOperation", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_STATE_MODELING_OPERATION(Object arg0) {
/* 1455 */     return localizer.localize(localizableWSDLMODELER_INVALID_STATE_MODELING_OPERATION(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_OPERATION_NAME(Object arg0) {
/* 1459 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.javaReservedWordNotAllowed.operationName", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_OPERATION_NAME(Object arg0) {
/* 1467 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_OPERATION_NAME(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_HEADER_NOT_ENCODED(Object arg0, Object arg1) {
/* 1471 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringHeader.notEncoded", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_HEADER_NOT_ENCODED(Object arg0, Object arg1) {
/* 1479 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_HEADER_NOT_ENCODED(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_DUPLICATE_FAULT_PART_NAME(Object arg0, Object arg1, Object arg2) {
/* 1483 */     return messageFactory.getMessage("wsdlmodeler.duplicate.fault.part.name", new Object[] { arg0, arg1, arg2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_DUPLICATE_FAULT_PART_NAME(Object arg0, Object arg1, Object arg2) {
/* 1491 */     return localizer.localize(localizableWSDLMODELER_DUPLICATE_FAULT_PART_NAME(arg0, arg1, arg2));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_OPERATION_MORE_THAN_ONE_PART_IN_MESSAGE(Object arg0) {
/* 1495 */     return messageFactory.getMessage("wsdlmodeler.invalid.operation.MoreThanOnePartInMessage", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_OPERATION_MORE_THAN_ONE_PART_IN_MESSAGE(Object arg0) {
/* 1503 */     return localizer.localize(localizableWSDLMODELER_INVALID_OPERATION_MORE_THAN_ONE_PART_IN_MESSAGE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_SOAP_BINDING_12(Object arg0) {
/* 1507 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringSOAPBinding12", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_SOAP_BINDING_12(Object arg0) {
/* 1516 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_SOAP_BINDING_12(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_FAULT_NOT_UNIQUE(Object arg0, Object arg1) {
/* 1520 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingFault.notUnique", new Object[] { arg0, arg1 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_FAULT_NOT_UNIQUE(Object arg0, Object arg1) {
/* 1528 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_FAULT_NOT_UNIQUE(arg0, arg1));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_INVALID_BINDING_OPERATION_OUTPUT_MISSING_SOAP_BODY(Object arg0) {
/* 1532 */     return messageFactory.getMessage("wsdlmodeler.invalid.bindingOperation.outputMissingSoapBody", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_INVALID_BINDING_OPERATION_OUTPUT_MISSING_SOAP_BODY(Object arg0) {
/* 1540 */     return localizer.localize(localizableWSDLMODELER_INVALID_BINDING_OPERATION_OUTPUT_MISSING_SOAP_BODY(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_HEADER_FAULT_NOT_LITERAL(Object arg0, Object arg1, Object arg2) {
/* 1544 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringHeaderFault.notLiteral", new Object[] { arg0, arg1, arg2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_HEADER_FAULT_NOT_LITERAL(Object arg0, Object arg1, Object arg2) {
/* 1552 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_HEADER_FAULT_NOT_LITERAL(arg0, arg1, arg2));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_MORE_THAN_ONE_PART_IN_OUTPUT_MESSAGE(Object arg0) {
/* 1556 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.cannotHandleMoreThanOnePartInOutputMessage", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_MORE_THAN_ONE_PART_IN_OUTPUT_MESSAGE(Object arg0) {
/* 1564 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_CANNOT_HANDLE_MORE_THAN_ONE_PART_IN_OUTPUT_MESSAGE(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_NO_OPERATIONS_IN_PORT(Object arg0) {
/* 1568 */     return messageFactory.getMessage("wsdlmodeler.warning.noOperationsInPort", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_NO_OPERATIONS_IN_PORT(Object arg0) {
/* 1576 */     return localizer.localize(localizableWSDLMODELER_WARNING_NO_OPERATIONS_IN_PORT(arg0));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_WRAPPER_STYLE(Object arg0, Object arg1, Object arg2) {
/* 1580 */     return messageFactory.getMessage("wsdlmodeler.warning.ignoringOperation.javaReservedWordNotAllowed.wrapperStyle", new Object[] { arg0, arg1, arg2 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_WRAPPER_STYLE(Object arg0, Object arg1, Object arg2) {
/* 1588 */     return localizer.localize(localizableWSDLMODELER_WARNING_IGNORING_OPERATION_JAVA_RESERVED_WORD_NOT_ALLOWED_WRAPPER_STYLE(arg0, arg1, arg2));
/*      */   }
/*      */   
/*      */   public static Localizable localizableWSDLMODELER_UNSOLVABLE_NAMING_CONFLICTS(Object arg0) {
/* 1592 */     return messageFactory.getMessage("wsdlmodeler.unsolvableNamingConflicts", new Object[] { arg0 });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String WSDLMODELER_UNSOLVABLE_NAMING_CONFLICTS(Object arg0) {
/* 1600 */     return localizer.localize(localizableWSDLMODELER_UNSOLVABLE_NAMING_CONFLICTS(arg0));
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\resources\ModelerMessages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */