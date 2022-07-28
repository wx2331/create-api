/*    */ package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.codemodel.internal.JCodeModel;
/*    */ import com.sun.tools.internal.xjc.reader.Ring;
/*    */ import com.sun.tools.internal.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.xml.internal.bind.annotation.XmlLocation;
/*    */ import com.sun.xml.internal.xsom.XSComponent;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractDeclarationImpl
/*    */   implements BIDeclaration
/*    */ {
/*    */   @XmlLocation
/*    */   Locator loc;
/*    */   protected BindInfo parent;
/*    */   private boolean isAcknowledged;
/*    */   
/*    */   @Deprecated
/*    */   protected AbstractDeclarationImpl(Locator loc) {
/* 73 */     this.isAcknowledged = false; this.loc = loc; } protected AbstractDeclarationImpl() { this.isAcknowledged = false; }
/*    */   public Locator getLocation() { return this.loc; }
/* 75 */   public void setParent(BindInfo p) { this.parent = p; } public final boolean isAcknowledged() { return this.isAcknowledged; }
/*    */   protected final XSComponent getOwner() { return this.parent.getOwner(); } protected final BGMBuilder getBuilder() {
/*    */     return this.parent.getBuilder();
/*    */   } protected final JCodeModel getCodeModel() {
/*    */     return (JCodeModel)Ring.get(JCodeModel.class);
/*    */   } public void onSetOwner() {} public Collection<BIDeclaration> getChildren() {
/* 81 */     return Collections.emptyList();
/*    */   }
/*    */   
/*    */   public void markAsAcknowledged() {
/* 85 */     this.isAcknowledged = true;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\AbstractDeclarationImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */