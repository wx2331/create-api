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
/*    */ public class Identifier
/*    */   extends Expression
/*    */ {
/*    */   private String name;
/*    */   private Object value;
/*    */   
/*    */   public Identifier(String paramString) {
/* 42 */     this.name = paramString;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 46 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setValue(Object paramObject) {
/* 50 */     this.value = paramObject;
/*    */   }
/*    */   
/*    */   public Object getValue() {
/* 54 */     return this.value;
/*    */   }
/*    */   
/*    */   public boolean isResolved() {
/* 58 */     return (this.value != null);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 62 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\jstat\Identifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */