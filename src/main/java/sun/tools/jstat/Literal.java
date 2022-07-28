/*    */ package sun.tools.jstat;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Literal
/*    */   extends Expression
/*    */ {
/*    */   private Object value;
/*    */   
/*    */   public Literal(Object paramObject) {
/* 41 */     this.value = paramObject;
/*    */   }
/*    */   
/*    */   public Object getValue() {
/* 45 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(Object paramObject) {
/* 49 */     this.value = paramObject;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 53 */     return this.value.toString();
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\Literal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */