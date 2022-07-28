/*    */ package org.relaxng.datatype.helpers;
/*    */ 
/*    */ import org.relaxng.datatype.Datatype;
/*    */ import org.relaxng.datatype.DatatypeException;
/*    */ import org.relaxng.datatype.DatatypeStreamingValidator;
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
/*    */ 
/*    */ 
/*    */ public final class StreamingValidatorImpl
/*    */   implements DatatypeStreamingValidator
/*    */ {
/* 63 */   private final StringBuffer buffer = new StringBuffer();
/*    */ 
/*    */   
/*    */   private final Datatype baseType;
/*    */ 
/*    */   
/*    */   private final ValidationContext context;
/*    */ 
/*    */   
/*    */   public void addCharacters(char[] buf, int start, int len) {
/* 73 */     this.buffer.append(buf, start, len);
/*    */   }
/*    */   
/*    */   public boolean isValid() {
/* 77 */     return this.baseType.isValid(this.buffer.toString(), this.context);
/*    */   }
/*    */   
/*    */   public void checkValid() throws DatatypeException {
/* 81 */     this.baseType.checkValid(this.buffer.toString(), this.context);
/*    */   }
/*    */   
/*    */   public StreamingValidatorImpl(Datatype baseType, ValidationContext context) {
/* 85 */     this.baseType = baseType;
/* 86 */     this.context = context;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\org\relaxng\datatype\helpers\StreamingValidatorImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */