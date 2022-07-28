/*    */ package sun.tools.tree;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import sun.tools.java.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ByteExpression
/*    */   extends IntegerExpression
/*    */ {
/*    */   public ByteExpression(long paramLong, byte paramByte) {
/* 42 */     super(62, paramLong, Type.tByte, paramByte);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void print(PrintStream paramPrintStream) {
/* 49 */     paramPrintStream.print(this.value + "b");
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\ByteExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */