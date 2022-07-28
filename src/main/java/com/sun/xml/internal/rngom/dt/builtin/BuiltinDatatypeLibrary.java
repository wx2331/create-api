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
/*    */ 
/*    */ public class BuiltinDatatypeLibrary
/*    */   implements DatatypeLibrary
/*    */ {
/*    */   private final DatatypeLibraryFactory factory;
/* 58 */   private DatatypeLibrary xsdDatatypeLibrary = null;
/*    */   
/*    */   BuiltinDatatypeLibrary(DatatypeLibraryFactory factory) {
/* 61 */     this.factory = factory;
/*    */   }
/*    */ 
/*    */   
/*    */   public DatatypeBuilder createDatatypeBuilder(String type) throws DatatypeException {
/* 66 */     this
/* 67 */       .xsdDatatypeLibrary = this.factory.createDatatypeLibrary("http://www.w3.org/2001/XMLSchema-datatypes");
/*    */     
/* 69 */     if (this.xsdDatatypeLibrary == null) {
/* 70 */       throw new DatatypeException();
/*    */     }
/* 72 */     if (type.equals("string") || type.equals("token")) {
/* 73 */       return new BuiltinDatatypeBuilder(this.xsdDatatypeLibrary
/* 74 */           .createDatatype(type));
/*    */     }
/* 76 */     throw new DatatypeException();
/*    */   }
/*    */   public Datatype createDatatype(String type) throws DatatypeException {
/* 79 */     return createDatatypeBuilder(type).createDatatype();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\dt\builtin\BuiltinDatatypeLibrary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */