/*     */ package com.sun.tools.internal.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.tools.internal.xjc.Options;
/*     */ import com.sun.tools.internal.xjc.generator.bean.field.FieldRenderer;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BIContent
/*     */ {
/*     */   protected final Element element;
/*     */   protected final BIElement parent;
/*     */   private final Options opts;
/*     */   
/*     */   private BIContent(Element e, BIElement _parent) {
/*  50 */     this.element = e;
/*  51 */     this.parent = _parent;
/*  52 */     this.opts = this.parent.parent.model.options;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FieldRenderer getRealization() {
/*  70 */     String v = DOMUtil.getAttribute(this.element, "collection");
/*  71 */     if (v == null) return null;
/*     */     
/*  73 */     v = v.trim();
/*  74 */     if (v.equals("array")) return this.opts.getFieldRendererFactory().getArray(); 
/*  75 */     if (v.equals("list")) {
/*  76 */       return this.opts.getFieldRendererFactory().getList(this.parent.parent.codeModel
/*  77 */           .ref(ArrayList.class));
/*     */     }
/*     */ 
/*     */     
/*  81 */     throw new InternalError("unexpected collection value: " + v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPropertyName() {
/*  91 */     String r = DOMUtil.getAttribute(this.element, "property");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  96 */     if (r != null) return r; 
/*  97 */     return DOMUtil.getAttribute(this.element, "name");
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
/*     */   public final JClass getType() {
/*     */     try {
/* 110 */       String type = DOMUtil.getAttribute(this.element, "supertype");
/* 111 */       if (type == null) return null;
/*     */ 
/*     */       
/* 114 */       int idx = type.lastIndexOf('.');
/* 115 */       if (idx < 0) return this.parent.parent.codeModel.ref(type); 
/* 116 */       return this.parent.parent.getTargetPackage().ref(type);
/* 117 */     } catch (ClassNotFoundException e) {
/*     */       
/* 119 */       throw new NoClassDefFoundError(e.getMessage());
/*     */     } 
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
/*     */ 
/*     */   
/*     */   static BIContent create(Element e, BIElement _parent) {
/* 134 */     return new BIContent(e, _parent);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\dtd\bindinfo\BIContent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */