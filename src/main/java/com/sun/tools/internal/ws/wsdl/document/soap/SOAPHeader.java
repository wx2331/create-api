/*     */ package com.sun.tools.internal.ws.wsdl.document.soap;
/*     */ 
/*     */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionImpl;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionVisitor;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.QNameAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.ValidationException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class SOAPHeader
/*     */   extends ExtensionImpl
/*     */ {
/*     */   private String _encodingStyle;
/*     */   private String _namespace;
/*     */   private String _part;
/*     */   private QName _message;
/*     */   private SOAPUse _use;
/*     */   private List _faults;
/*     */   
/*     */   public SOAPHeader(Locator locator) {
/*  44 */     super(locator);
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
/* 153 */     this._use = SOAPUse.LITERAL;
/*     */     this._faults = new ArrayList();
/*     */   }
/*     */   
/*     */   public void add(SOAPHeaderFault fault) {
/*     */     this._faults.add(fault);
/*     */   }
/*     */   
/*     */   public Iterator faults() {
/*     */     return this._faults.iterator();
/*     */   }
/*     */   
/*     */   public QName getElementName() {
/*     */     return SOAPConstants.QNAME_HEADER;
/*     */   }
/*     */   
/*     */   public String getNamespace() {
/*     */     return this._namespace;
/*     */   }
/*     */   
/*     */   public void setNamespace(String s) {
/*     */     this._namespace = s;
/*     */   }
/*     */   
/*     */   public SOAPUse getUse() {
/*     */     return this._use;
/*     */   }
/*     */   
/*     */   public void setUse(SOAPUse u) {
/*     */     this._use = u;
/*     */   }
/*     */   
/*     */   public boolean isEncoded() {
/*     */     return (this._use == SOAPUse.ENCODED);
/*     */   }
/*     */   
/*     */   public boolean isLiteral() {
/*     */     return (this._use == SOAPUse.LITERAL);
/*     */   }
/*     */   
/*     */   public String getEncodingStyle() {
/*     */     return this._encodingStyle;
/*     */   }
/*     */   
/*     */   public void setEncodingStyle(String s) {
/*     */     this._encodingStyle = s;
/*     */   }
/*     */   
/*     */   public String getPart() {
/*     */     return this._part;
/*     */   }
/*     */   
/*     */   public void setMessage(QName message) {
/*     */     this._message = message;
/*     */   }
/*     */   
/*     */   public QName getMessage() {
/*     */     return this._message;
/*     */   }
/*     */   
/*     */   public void setPart(String s) {
/*     */     this._part = s;
/*     */   }
/*     */   
/*     */   public void withAllSubEntitiesDo(EntityAction action) {
/*     */     super.withAllSubEntitiesDo(action);
/*     */     for (Iterator<Entity> iter = this._faults.iterator(); iter.hasNext();)
/*     */       action.perform(iter.next()); 
/*     */   }
/*     */   
/*     */   public void withAllQNamesDo(QNameAction action) {
/*     */     super.withAllQNamesDo(action);
/*     */     if (this._message != null)
/*     */       action.perform(this._message); 
/*     */   }
/*     */   
/*     */   public void accept(ExtensionVisitor visitor) throws Exception {
/*     */     visitor.preVisit((TWSDLExtension)this);
/*     */     for (Iterator<SOAPHeaderFault> iter = this._faults.iterator(); iter.hasNext();)
/*     */       ((SOAPHeaderFault)iter.next()).accept(visitor); 
/*     */     visitor.postVisit((TWSDLExtension)this);
/*     */   }
/*     */   
/*     */   public void validateThis() {
/*     */     if (this._message == null)
/*     */       failValidation("validation.missingRequiredAttribute", "message"); 
/*     */     if (this._part == null)
/*     */       failValidation("validation.missingRequiredAttribute", "part"); 
/*     */     if (this._use == SOAPUse.ENCODED)
/*     */       throw new ValidationException("validation.unsupportedUse.encoded", new Object[] { Integer.valueOf(getLocator().getLineNumber()), getLocator().getSystemId() }); 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\soap\SOAPHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */