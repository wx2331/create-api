/*    */ package com.sun.xml.internal.rngom.dt.builtin;
/*    */ 
/*    */ import org.relaxng.datatype.Datatype;
/*    */ import org.relaxng.datatype.DatatypeBuilder;
/*    */ import org.relaxng.datatype.DatatypeException;
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
/*    */ class CompatibilityDatatypeLibrary
/*    */   implements DatatypeLibrary
/*    */ {
/*    */   private final DatatypeLibraryFactory factory;
/* 57 */   private DatatypeLibrary xsdDatatypeLibrary = null;
/*    */   
/*    */   CompatibilityDatatypeLibrary(DatatypeLibraryFactory factory) {
/* 60 */     this.factory = factory;
/*    */   }
/*    */ 
/*    */   
/*    */   public DatatypeBuilder createDatatypeBuilder(String type) throws DatatypeException {
/* 65 */     if (type.equals("ID") || type
/* 66 */       .equals("IDREF") || type
/* 67 */       .equals("IDREFS")) {
/* 68 */       if (this.xsdDatatypeLibrary == null) {
/* 69 */         this
/* 70 */           .xsdDatatypeLibrary = this.factory.createDatatypeLibrary("http://www.w3.org/2001/XMLSchema-datatypes");
/*    */         
/* 72 */         if (this.xsdDatatypeLibrary == null)
/* 73 */           throw new DatatypeException(); 
/*    */       } 
/* 75 */       return this.xsdDatatypeLibrary.createDatatypeBuilder(type);
/*    */     } 
/* 77 */     throw new DatatypeException();
/*    */   }
/*    */   
/*    */   public Datatype createDatatype(String type) throws DatatypeException {
/* 81 */     return createDatatypeBuilder(type).createDatatype();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\dt\builtin\CompatibilityDatatypeLibrary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */