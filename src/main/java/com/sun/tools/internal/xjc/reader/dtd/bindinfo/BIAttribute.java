/*     */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.generator.bean.field.FieldRenderer;
/*     */ import com.sun.tools.internal.xjc.generator.bean.field.FieldRendererFactory;
/*     */ import java.util.ArrayList;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BIAttribute
/*     */ {
/*     */   private final BIElement parent;
/*     */   private final Element element;
/*     */   
/*     */   BIAttribute(BIElement _parent, Element _e) {
/*  45 */     this.parent = _parent;
/*  46 */     this.element = _e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String name() {
/*  54 */     return this.element.getAttribute("name");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIConversion getConversion() {
/*  66 */     if (this.element.getAttributeNode("convert") == null) {
/*  67 */       return null;
/*     */     }
/*  69 */     String cnv = this.element.getAttribute("convert");
/*  70 */     return this.parent.conversion(cnv);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FieldRenderer getRealization() {
/*  80 */     Attr a = this.element.getAttributeNode("collection");
/*  81 */     if (a == null) return null;
/*     */     
/*  83 */     String v = this.element.getAttribute("collection").trim();
/*     */     
/*  85 */     FieldRendererFactory frf = this.parent.parent.model.options.getFieldRendererFactory();
/*  86 */     if (v.equals("array")) return frf.getArray(); 
/*  87 */     if (v.equals("list")) {
/*  88 */       return frf.getList(this.parent.parent.codeModel
/*  89 */           .ref(ArrayList.class));
/*     */     }
/*     */ 
/*     */     
/*  93 */     throw new InternalError("unexpected collection value: " + v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPropertyName() {
/* 103 */     String r = DOMUtil.getAttribute(this.element, "property");
/* 104 */     if (r != null) return r; 
/* 105 */     return name();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\dtd\bindinfo\BIAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */