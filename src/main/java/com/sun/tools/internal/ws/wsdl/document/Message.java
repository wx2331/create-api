/*     */ package com.sun.tools.internal.ws.wsdl.document;
/*     */ 
/*     */ import com.sun.tools.internal.ws.resources.WsdlMessages;
/*     */ import com.sun.tools.internal.ws.wscompile.AbortException;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Defining;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.EntityAction;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.GlobalEntity;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Kind;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class Message
/*     */   extends GlobalEntity
/*     */ {
/*     */   private Documentation _documentation;
/*     */   private List<MessagePart> _parts;
/*     */   private Map<String, MessagePart> _partsByName;
/*     */   
/*     */   public Message(Defining defining, Locator locator, ErrorReceiver errReceiver) {
/*  45 */     super(defining, locator, errReceiver);
/*  46 */     this._parts = new ArrayList<>();
/*  47 */     this._partsByName = new HashMap<>();
/*     */   }
/*     */   
/*     */   public void add(MessagePart part) {
/*  51 */     if (this._partsByName.get(part.getName()) != null) {
/*  52 */       this.errorReceiver.error(part.getLocator(), WsdlMessages.VALIDATION_DUPLICATE_PART_NAME(getName(), part.getName()));
/*  53 */       throw new AbortException();
/*     */     } 
/*     */     
/*  56 */     if (part.getDescriptor() != null && part.getDescriptorKind() != null) {
/*  57 */       this._partsByName.put(part.getName(), part);
/*  58 */       this._parts.add(part);
/*     */     } else {
/*  60 */       this.errorReceiver.warning(part.getLocator(), WsdlMessages.PARSING_ELEMENT_OR_TYPE_REQUIRED(part.getName()));
/*     */     } 
/*     */   }
/*     */   public Iterator<MessagePart> parts() {
/*  64 */     return this._parts.iterator();
/*     */   }
/*     */   
/*     */   public List<MessagePart> getParts() {
/*  68 */     return this._parts;
/*     */   }
/*     */   
/*     */   public MessagePart getPart(String name) {
/*  72 */     return this._partsByName.get(name);
/*     */   }
/*     */   
/*     */   public int numParts() {
/*  76 */     return this._parts.size();
/*     */   }
/*     */   
/*     */   public Kind getKind() {
/*  80 */     return Kinds.MESSAGE;
/*     */   }
/*     */   
/*     */   public QName getElementName() {
/*  84 */     return WSDLConstants.QNAME_MESSAGE;
/*     */   }
/*     */   
/*     */   public Documentation getDocumentation() {
/*  88 */     return this._documentation;
/*     */   }
/*     */   
/*     */   public void setDocumentation(Documentation d) {
/*  92 */     this._documentation = d;
/*     */   }
/*     */   
/*     */   public void withAllSubEntitiesDo(EntityAction action) {
/*  96 */     super.withAllSubEntitiesDo(action);
/*     */     
/*  98 */     for (Iterator<MessagePart> iter = this._parts.iterator(); iter.hasNext();) {
/*  99 */       action.perform(iter.next());
/*     */     }
/*     */   }
/*     */   
/*     */   public void accept(WSDLDocumentVisitor visitor) throws Exception {
/* 104 */     visitor.preVisit(this);
/* 105 */     for (Iterator<MessagePart> iter = this._parts.iterator(); iter.hasNext();) {
/* 106 */       ((MessagePart)iter.next()).accept(visitor);
/*     */     }
/* 108 */     visitor.postVisit(this);
/*     */   }
/*     */   
/*     */   public void validateThis() {
/* 112 */     if (getName() == null) {
/* 113 */       this.errorReceiver.error(getLocator(), WsdlMessages.VALIDATION_MISSING_REQUIRED_ATTRIBUTE("name", "wsdl:message"));
/* 114 */       throw new AbortException();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\Message.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */