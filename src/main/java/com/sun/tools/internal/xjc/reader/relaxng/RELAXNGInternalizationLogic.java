/*    */ package com.sun.tools.internal.xjc.reader.relaxng;
/*    */ 
/*    */ import com.sun.tools.internal.xjc.reader.internalizer.AbstractReferenceFinderImpl;
/*    */ import com.sun.tools.internal.xjc.reader.internalizer.DOMForest;
/*    */ import com.sun.tools.internal.xjc.reader.internalizer.InternalizationLogic;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.helpers.XMLFilterImpl;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RELAXNGInternalizationLogic
/*    */   implements InternalizationLogic
/*    */ {
/*    */   private static final class ReferenceFinder
/*    */     extends AbstractReferenceFinderImpl
/*    */   {
/*    */     ReferenceFinder(DOMForest parent) {
/* 51 */       super(parent);
/*    */     }
/*    */     
/*    */     protected String findExternalResource(String nsURI, String localName, Attributes atts) {
/* 55 */       if ("http://relaxng.org/ns/structure/1.0".equals(nsURI) && ("include"
/* 56 */         .equals(localName) || "externalRef".equals(localName))) {
/* 57 */         return atts.getValue("href");
/*    */       }
/* 59 */       return null;
/*    */     }
/*    */   }
/*    */   
/*    */   public XMLFilterImpl createExternalReferenceFinder(DOMForest parent) {
/* 64 */     return (XMLFilterImpl)new ReferenceFinder(parent);
/*    */   }
/*    */   
/*    */   public boolean checkIfValidTargetNode(DOMForest parent, Element bindings, Element target) {
/* 68 */     return "http://relaxng.org/ns/structure/1.0".equals(target.getNamespaceURI());
/*    */   }
/*    */ 
/*    */   
/*    */   public Element refineTarget(Element target) {
/* 73 */     return target;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\relaxng\RELAXNGInternalizationLogic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */