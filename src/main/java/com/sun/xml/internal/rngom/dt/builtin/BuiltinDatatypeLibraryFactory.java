/*    */ package com.sun.xml.internal.rngom.dt.builtin;
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
/*    */ 
/*    */ 
/*    */ public class BuiltinDatatypeLibraryFactory
/*    */   implements DatatypeLibraryFactory
/*    */ {
/*    */   private final DatatypeLibrary builtinDatatypeLibrary;
/*    */   private final DatatypeLibrary compatibilityDatatypeLibrary;
/*    */   private final DatatypeLibraryFactory core;
/*    */   
/*    */   public BuiltinDatatypeLibraryFactory(DatatypeLibraryFactory coreFactory) {
/* 66 */     this.builtinDatatypeLibrary = new BuiltinDatatypeLibrary(coreFactory);
/* 67 */     this.compatibilityDatatypeLibrary = new CompatibilityDatatypeLibrary(coreFactory);
/* 68 */     this.core = coreFactory;
/*    */   }
/*    */   
/*    */   public DatatypeLibrary createDatatypeLibrary(String uri) {
/* 72 */     if (uri.equals(""))
/* 73 */       return this.builtinDatatypeLibrary; 
/* 74 */     if (uri.equals("http://relaxng.org/ns/compatibility/datatypes/1.0"))
/* 75 */       return this.compatibilityDatatypeLibrary; 
/* 76 */     return this.core.createDatatypeLibrary(uri);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\dt\builtin\BuiltinDatatypeLibraryFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */