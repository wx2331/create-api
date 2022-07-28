/*    */ package com.sun.xml.internal.rngom.dt;
/*    */ 
/*    */ import org.relaxng.datatype.DatatypeLibrary;
/*    */ import org.relaxng.datatype.DatatypeLibraryFactory;
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
/*    */ public class CascadingDatatypeLibraryFactory
/*    */   implements DatatypeLibraryFactory
/*    */ {
/*    */   private final DatatypeLibraryFactory factory1;
/*    */   private final DatatypeLibraryFactory factory2;
/*    */   
/*    */   public CascadingDatatypeLibraryFactory(DatatypeLibraryFactory factory1, DatatypeLibraryFactory factory2) {
/* 62 */     this.factory1 = factory1;
/* 63 */     this.factory2 = factory2;
/*    */   }
/*    */   
/*    */   public DatatypeLibrary createDatatypeLibrary(String namespaceURI) {
/* 67 */     DatatypeLibrary lib = this.factory1.createDatatypeLibrary(namespaceURI);
/* 68 */     if (lib == null)
/* 69 */       lib = this.factory2.createDatatypeLibrary(namespaceURI); 
/* 70 */     return lib;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\dt\CascadingDatatypeLibraryFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */