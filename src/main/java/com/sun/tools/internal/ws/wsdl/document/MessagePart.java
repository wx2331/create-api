/*     */ package com.sun.tools.internal.ws.wsdl.document;
/*     */ 
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityReferenceAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Kind;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.QNameAction;
/*     */ import javax.jws.WebParam;
/*     */ import javax.xml.namespace.QName;
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
/*     */ public class MessagePart
/*     */   extends Entity
/*     */ {
/*     */   public static final int SOAP_BODY_BINDING = 1;
/*     */   public static final int SOAP_HEADER_BINDING = 2;
/*     */   public static final int SOAP_HEADERFAULT_BINDING = 3;
/*     */   public static final int SOAP_FAULT_BINDING = 4;
/*     */   public static final int WSDL_MIME_BINDING = 5;
/*     */   public static final int PART_NOT_BOUNDED = -1;
/*     */   private boolean isRet;
/*     */   private String _name;
/*     */   private QName _descriptor;
/*     */   private Kind _descriptorKind;
/*     */   private int _bindingKind;
/*     */   private WebParam.Mode mode;
/*     */   
/*     */   public MessagePart(Locator locator) {
/*  52 */     super(locator);
/*     */   }
/*     */   
/*     */   public String getName() {
/*  56 */     return this._name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  60 */     this._name = name;
/*     */   }
/*     */   
/*     */   public QName getDescriptor() {
/*  64 */     return this._descriptor;
/*     */   }
/*     */   
/*     */   public void setDescriptor(QName n) {
/*  68 */     this._descriptor = n;
/*     */   }
/*     */   
/*     */   public Kind getDescriptorKind() {
/*  72 */     return this._descriptorKind;
/*     */   }
/*     */   
/*     */   public void setDescriptorKind(Kind k) {
/*  76 */     this._descriptorKind = k;
/*     */   }
/*     */   
/*     */   public QName getElementName() {
/*  80 */     return WSDLConstants.QNAME_PART;
/*     */   }
/*     */   
/*     */   public int getBindingExtensibilityElementKind() {
/*  84 */     return this._bindingKind;
/*     */   }
/*     */   
/*     */   public void setBindingExtensibilityElementKind(int kind) {
/*  88 */     this._bindingKind = kind;
/*     */   }
/*     */   
/*     */   public void withAllQNamesDo(QNameAction action) {
/*  92 */     if (this._descriptor != null) {
/*  93 */       action.perform(this._descriptor);
/*     */     }
/*     */   }
/*     */   
/*     */   public void withAllEntityReferencesDo(EntityReferenceAction action) {
/*  98 */     super.withAllEntityReferencesDo(action);
/*  99 */     if (this._descriptor != null && this._descriptorKind != null) {
/* 100 */       action.perform(this._descriptorKind, this._descriptor);
/*     */     }
/*     */   }
/*     */   
/*     */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/* 105 */     visitor.visit(this);
/*     */   }
/*     */   
/*     */   public void validateThis() {
/* 109 */     if (this._descriptor != null && this._descriptor.getLocalPart().equals("")) {
/* 110 */       failValidation("validation.invalidElement", this._descriptor.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public void setMode(WebParam.Mode mode) {
/* 115 */     this.mode = mode;
/*     */   }
/*     */   
/*     */   public WebParam.Mode getMode() {
/* 119 */     return this.mode;
/*     */   }
/*     */   
/*     */   public boolean isINOUT() {
/* 123 */     if (this.mode != null)
/* 124 */       return (this.mode == WebParam.Mode.INOUT); 
/* 125 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isIN() {
/* 129 */     if (this.mode != null)
/* 130 */       return (this.mode == WebParam.Mode.IN); 
/* 131 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isOUT() {
/* 135 */     if (this.mode != null)
/* 136 */       return (this.mode == WebParam.Mode.OUT); 
/* 137 */     return false;
/*     */   }
/*     */   
/*     */   public void setReturn(boolean ret) {
/* 141 */     this.isRet = ret;
/*     */   }
/*     */   
/*     */   public boolean isReturn() {
/* 145 */     return this.isRet;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\MessagePart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */