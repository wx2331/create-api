/*     */ package com.sun.tools.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtensible;
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLParserContext;
/*     */ import com.sun.tools.internal.ws.resources.ModelerMessages;
/*     */ import com.sun.tools.internal.ws.resources.WsdlMessages;
/*     */ import com.sun.tools.internal.ws.util.xml.XmlUtil;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Fault;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Input;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Output;
/*     */ import com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionAddressingConstants;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.Locator;
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
/*     */ public class MemberSubmissionAddressingExtensionHandler
/*     */   extends W3CAddressingExtensionHandler
/*     */ {
/*     */   private ErrorReceiver errReceiver;
/*     */   private boolean extensionModeOn;
/*     */   
/*     */   public MemberSubmissionAddressingExtensionHandler(Map<String, AbstractExtensionHandler> extensionHandlerMap, ErrorReceiver env, boolean extensionModeOn) {
/*  56 */     super(extensionHandlerMap, env);
/*  57 */     this.errReceiver = env;
/*  58 */     this.extensionModeOn = extensionModeOn;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespaceURI() {
/*  63 */     return AddressingVersion.MEMBER.wsdlNsUri;
/*     */   }
/*     */   
/*     */   protected QName getWSDLExtensionQName() {
/*  67 */     return AddressingVersion.MEMBER.wsdlExtensionTag;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handlePortExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handleInputExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/*  78 */     if (this.extensionModeOn) {
/*  79 */       warn(context.getLocation(e));
/*  80 */       String actionValue = XmlUtil.getAttributeNSOrNull(e, MemberSubmissionAddressingConstants.WSA_ACTION_QNAME);
/*  81 */       if (actionValue == null || actionValue.equals("")) {
/*  82 */         return warnEmptyAction(parent, context.getLocation(e));
/*     */       }
/*  84 */       ((Input)parent).setAction(actionValue);
/*  85 */       return true;
/*     */     } 
/*  87 */     return fail(context.getLocation(e));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean fail(Locator location) {
/*  92 */     this.errReceiver.warning(location, 
/*  93 */         ModelerMessages.WSDLMODELER_INVALID_IGNORING_MEMBER_SUBMISSION_ADDRESSING(AddressingVersion.MEMBER.nsUri, "http://www.w3.org/2007/05/addressing/metadata"));
/*     */     
/*  95 */     return false;
/*     */   }
/*     */   
/*     */   private void warn(Locator location) {
/*  99 */     this.errReceiver.warning(location, 
/* 100 */         ModelerMessages.WSDLMODELER_WARNING_MEMBER_SUBMISSION_ADDRESSING_USED(AddressingVersion.MEMBER.nsUri, "http://www.w3.org/2007/05/addressing/metadata"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleOutputExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 106 */     if (this.extensionModeOn) {
/* 107 */       warn(context.getLocation(e));
/* 108 */       String actionValue = XmlUtil.getAttributeNSOrNull(e, MemberSubmissionAddressingConstants.WSA_ACTION_QNAME);
/* 109 */       if (actionValue == null || actionValue.equals("")) {
/* 110 */         return warnEmptyAction(parent, context.getLocation(e));
/*     */       }
/* 112 */       ((Output)parent).setAction(actionValue);
/* 113 */       return true;
/*     */     } 
/* 115 */     return fail(context.getLocation(e));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleFaultExtension(TWSDLParserContext context, TWSDLExtensible parent, Element e) {
/* 121 */     if (this.extensionModeOn) {
/* 122 */       warn(context.getLocation(e));
/* 123 */       String actionValue = XmlUtil.getAttributeNSOrNull(e, MemberSubmissionAddressingConstants.WSA_ACTION_QNAME);
/* 124 */       if (actionValue == null || actionValue.equals("")) {
/* 125 */         this.errReceiver.warning(context.getLocation(e), WsdlMessages.WARNING_FAULT_EMPTY_ACTION(parent.getNameValue(), parent.getWSDLElementName().getLocalPart(), parent.getParent().getNameValue()));
/* 126 */         return false;
/*     */       } 
/* 128 */       ((Fault)parent).setAction(actionValue);
/* 129 */       return true;
/*     */     } 
/* 131 */     return fail(context.getLocation(e));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean warnEmptyAction(TWSDLExtensible parent, Locator pos) {
/* 136 */     this.errReceiver.warning(pos, WsdlMessages.WARNING_INPUT_OUTPUT_EMPTY_ACTION(parent.getWSDLElementName().getLocalPart(), parent.getParent().getNameValue()));
/* 137 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\parser\MemberSubmissionAddressingExtensionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */