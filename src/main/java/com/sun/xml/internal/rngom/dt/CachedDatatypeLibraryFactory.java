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
/*    */ 
/*    */ public class CachedDatatypeLibraryFactory
/*    */   implements DatatypeLibraryFactory
/*    */ {
/*    */   private String lastUri;
/*    */   private DatatypeLibrary lastLib;
/*    */   private final DatatypeLibraryFactory core;
/*    */   
/*    */   public CachedDatatypeLibraryFactory(DatatypeLibraryFactory core) {
/* 64 */     this.core = core;
/*    */   }
/*    */   
/*    */   public DatatypeLibrary createDatatypeLibrary(String namespaceURI) {
/* 68 */     if (this.lastUri == namespaceURI) {
/* 69 */       return this.lastLib;
/*    */     }
/* 71 */     this.lastUri = namespaceURI;
/* 72 */     this.lastLib = this.core.createDatatypeLibrary(namespaceURI);
/* 73 */     return this.lastLib;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\dt\CachedDatatypeLibraryFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */