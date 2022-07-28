/*    */ package org.relaxng.datatype.helpers;
/*    */ 
/*    */ import org.relaxng.datatype.Datatype;
/*    */ import org.relaxng.datatype.DatatypeBuilder;
/*    */ import org.relaxng.datatype.DatatypeException;
/*    */ import org.relaxng.datatype.ValidationContext;
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
/*    */ public final class ParameterlessDatatypeBuilder
/*    */   implements DatatypeBuilder
/*    */ {
/*    */   private final Datatype baseType;
/*    */   
/*    */   public ParameterlessDatatypeBuilder(Datatype baseType) {
/* 64 */     this.baseType = baseType;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParameter(String name, String strValue, ValidationContext context) throws DatatypeException {
/* 69 */     throw new DatatypeException();
/*    */   }
/*    */   
/*    */   public Datatype createDatatype() throws DatatypeException {
/* 73 */     return this.baseType;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\org\relaxng\datatype\helpers\ParameterlessDatatypeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */