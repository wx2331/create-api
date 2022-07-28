/*    */ package com.sun.tools.internal.ws.wsdl.document.mime;
/*    */ 
/*    */ import com.sun.tools.internal.ws.api.wsdl.TWSDLExtension;
/*    */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*    */ import com.sun.tools.internal.ws.wsdl.framework.EntityAction;
/*    */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionImpl;
/*    */ import com.sun.tools.internal.ws.wsdl.framework.ExtensionVisitor;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MIMEMultipartRelated
/*    */   extends ExtensionImpl
/*    */ {
/*    */   private List<MIMEPart> _parts;
/*    */   
/*    */   public MIMEMultipartRelated(Locator locator) {
/* 47 */     super(locator);
/* 48 */     this._parts = new ArrayList<>();
/*    */   }
/*    */   
/*    */   public QName getElementName() {
/* 52 */     return MIMEConstants.QNAME_MULTIPART_RELATED;
/*    */   }
/*    */   
/*    */   public void add(MIMEPart part) {
/* 56 */     this._parts.add(part);
/*    */   }
/*    */   
/*    */   public Iterable<MIMEPart> getParts() {
/* 60 */     return this._parts;
/*    */   }
/*    */   
/*    */   public void withAllSubEntitiesDo(EntityAction action) {
/* 64 */     super.withAllSubEntitiesDo(action);
/*    */     
/* 66 */     for (Iterator<MIMEPart> iter = this._parts.iterator(); iter.hasNext();) {
/* 67 */       action.perform((Entity)iter.next());
/*    */     }
/*    */   }
/*    */   
/*    */   public void accept(ExtensionVisitor visitor) throws Exception {
/* 72 */     visitor.preVisit((TWSDLExtension)this);
/* 73 */     visitor.postVisit((TWSDLExtension)this);
/*    */   }
/*    */   
/*    */   public void validateThis() {}
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\mime\MIMEMultipartRelated.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */