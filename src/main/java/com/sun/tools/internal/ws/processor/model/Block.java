/*    */ package com.sun.tools.internal.ws.processor.model;
/*    */ 
/*    */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Block
/*    */   extends ModelObject
/*    */ {
/*    */   public static final int UNBOUND = 0;
/*    */   public static final int BODY = 1;
/*    */   public static final int HEADER = 2;
/*    */   public static final int ATTACHMENT = 3;
/*    */   private final QName name;
/*    */   private AbstractType type;
/*    */   private int location;
/*    */   
/*    */   public Block(QName name, AbstractType type, Entity entity) {
/* 46 */     super(entity);
/* 47 */     this.name = name;
/* 48 */     this.type = type;
/*    */   }
/*    */   
/*    */   public QName getName() {
/* 52 */     return this.name;
/*    */   }
/*    */   
/*    */   public AbstractType getType() {
/* 56 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(AbstractType type) {
/* 60 */     this.type = type;
/*    */   }
/*    */   
/*    */   public int getLocation() {
/* 64 */     return this.location;
/*    */   }
/*    */   
/*    */   public void setLocation(int i) {
/* 68 */     this.location = i;
/*    */   }
/*    */   
/*    */   public void accept(ModelVisitor visitor) throws Exception {
/* 72 */     visitor.visit(this);
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\Block.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */