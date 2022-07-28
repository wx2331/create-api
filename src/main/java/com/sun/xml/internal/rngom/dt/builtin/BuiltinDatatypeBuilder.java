/*    */ package com.sun.xml.internal.rngom.dt.builtin;
/*    */ 
/*    */ import com.sun.xml.internal.rngom.util.Localizer;
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
/*    */ class BuiltinDatatypeBuilder
/*    */   implements DatatypeBuilder
/*    */ {
/*    */   private final Datatype dt;
/* 58 */   private static final Localizer localizer = new Localizer(BuiltinDatatypeBuilder.class);
/*    */   
/*    */   BuiltinDatatypeBuilder(Datatype dt) {
/* 61 */     this.dt = dt;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addParameter(String name, String value, ValidationContext context) throws DatatypeException {
/* 67 */     throw new DatatypeException(localizer.message("builtin_param"));
/*    */   }
/*    */   
/*    */   public Datatype createDatatype() {
/* 71 */     return this.dt;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\dt\builtin\BuiltinDatatypeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */