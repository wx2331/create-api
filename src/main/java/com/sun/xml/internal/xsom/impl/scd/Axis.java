/*     */ package com.sun.xml.internal.xsom.impl.scd;
/*     */ 
/*     */ import com.sun.xml.internal.xsom.XSAnnotation;
/*     */ import com.sun.xml.internal.xsom.XSAttContainer;
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
/*     */ import com.sun.xml.internal.xsom.XSComponent;
/*     */ import com.sun.xml.internal.xsom.XSContentType;
/*     */ import com.sun.xml.internal.xsom.XSElementDecl;
/*     */ import com.sun.xml.internal.xsom.XSFacet;
/*     */ import com.sun.xml.internal.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.internal.xsom.XSListSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSModelGroup;
/*     */ import com.sun.xml.internal.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSNotation;
/*     */ import com.sun.xml.internal.xsom.XSParticle;
/*     */ import com.sun.xml.internal.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSSchema;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.XSXPath;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public interface Axis<T extends XSComponent>
/*     */ {
/*  79 */   public static final Axis<XSSchema> ROOT = new Axis<XSSchema>() {
/*     */       public Iterator<XSSchema> iterator(XSComponent contextNode) {
/*  81 */         return contextNode.getRoot().iterateSchema();
/*     */       }
/*     */       
/*     */       public Iterator<XSSchema> iterator(Iterator<? extends XSComponent> contextNodes) {
/*  85 */         if (!contextNodes.hasNext()) {
/*  86 */           return Iterators.empty();
/*     */         }
/*     */         
/*  89 */         return iterator(contextNodes.next());
/*     */       }
/*     */       
/*     */       public boolean isModelGroup() {
/*  93 */         return false;
/*     */       }
/*     */       
/*     */       public String toString() {
/*  97 */         return "root::";
/*     */       }
/*     */     };
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
/* 110 */   public static final Axis<XSComponent> INTERMEDIATE_SKIP = new AbstractAxisImpl<XSComponent>() {
/*     */       public Iterator<XSComponent> elementDecl(XSElementDecl decl) {
/* 112 */         XSComplexType ct = decl.getType().asComplexType();
/* 113 */         if (ct == null) {
/* 114 */           return empty();
/*     */         }
/*     */         
/* 117 */         return new Iterators.Union<>(singleton((XSComponent)ct), complexType(ct));
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<XSComponent> modelGroupDecl(XSModelGroupDecl decl) {
/* 122 */         return descendants(decl.getModelGroup());
/*     */       }
/*     */       
/*     */       public Iterator<XSComponent> particle(XSParticle particle) {
/* 126 */         return descendants(particle.getTerm().asModelGroup());
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       private Iterator<XSComponent> descendants(XSModelGroup mg) {
/* 135 */         List<XSComponent> r = new ArrayList<>();
/* 136 */         visit(mg, r);
/* 137 */         return r.iterator();
/*     */       }
/*     */ 
/*     */       
/*     */       private void visit(XSModelGroup mg, List<XSComponent> r) {
/* 142 */         r.add(mg);
/* 143 */         for (XSParticle p : mg) {
/* 144 */           XSModelGroup child = p.getTerm().asModelGroup();
/* 145 */           if (child != null)
/* 146 */             visit(child, r); 
/*     */         } 
/*     */       }
/*     */       
/*     */       public String toString() {
/* 151 */         return "(intermediateSkip)";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   public static final Axis<XSComponent> DESCENDANTS = new Axis<XSComponent>() {
/*     */       public Iterator<XSComponent> iterator(XSComponent contextNode) {
/* 163 */         return (new Visitor()).iterator(contextNode);
/*     */       }
/*     */       public Iterator<XSComponent> iterator(Iterator<? extends XSComponent> contextNodes) {
/* 166 */         return (new Visitor()).iterator(contextNodes);
/*     */       }
/*     */       
/*     */       public boolean isModelGroup() {
/* 170 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       final class Visitor
/*     */         extends AbstractAxisImpl<XSComponent>
/*     */       {
/* 177 */         private final Set<XSComponent> visited = new HashSet<>();
/*     */ 
/*     */         
/*     */         final class Recursion
/*     */           extends Iterators.Map<XSComponent, XSComponent>
/*     */         {
/*     */           public Recursion(Iterator<? extends XSComponent> core) {
/* 184 */             super(core);
/*     */           }
/*     */           
/*     */           protected Iterator<XSComponent> apply(XSComponent u) {
/* 188 */             return Axis.DESCENDANTS.iterator(u);
/*     */           } }
/*     */         
/*     */         public Iterator<XSComponent> schema(XSSchema schema) {
/* 192 */           if (this.visited.add(schema)) {
/* 193 */             return ret((XSComponent)schema, new Recursion(schema.iterateElementDecls()));
/*     */           }
/* 195 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> elementDecl(XSElementDecl decl) {
/* 199 */           if (this.visited.add(decl)) {
/* 200 */             return ret((XSComponent)decl, iterator((XSComponent)decl.getType()));
/*     */           }
/* 202 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> simpleType(XSSimpleType type) {
/* 206 */           if (this.visited.add(type)) {
/* 207 */             return ret((XSComponent)type, (Iterator)FACET.iterator((XSComponent)type));
/*     */           }
/* 209 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> complexType(XSComplexType type) {
/* 213 */           if (this.visited.add(type)) {
/* 214 */             return ret((XSComponent)type, iterator((XSComponent)type.getContentType()));
/*     */           }
/* 216 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> particle(XSParticle particle) {
/* 220 */           if (this.visited.add(particle)) {
/* 221 */             return ret((XSComponent)particle, iterator((XSComponent)particle.getTerm()));
/*     */           }
/* 223 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> modelGroupDecl(XSModelGroupDecl decl) {
/* 227 */           if (this.visited.add(decl)) {
/* 228 */             return ret((XSComponent)decl, iterator((XSComponent)decl.getModelGroup()));
/*     */           }
/* 230 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> modelGroup(XSModelGroup group) {
/* 234 */           if (this.visited.add(group)) {
/* 235 */             return ret((XSComponent)group, new Recursion(group.iterator()));
/*     */           }
/* 237 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> attGroupDecl(XSAttGroupDecl decl) {
/* 241 */           if (this.visited.add(decl)) {
/* 242 */             return ret((XSComponent)decl, new Recursion(decl.iterateAttributeUses()));
/*     */           }
/* 244 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> attributeUse(XSAttributeUse use) {
/* 248 */           if (this.visited.add(use)) {
/* 249 */             return ret((XSComponent)use, iterator((XSComponent)use.getDecl()));
/*     */           }
/* 251 */           return empty();
/*     */         }
/*     */         
/*     */         public Iterator<XSComponent> attributeDecl(XSAttributeDecl decl) {
/* 255 */           if (this.visited.add(decl)) {
/* 256 */             return ret((XSComponent)decl, iterator((XSComponent)decl.getType()));
/*     */           }
/* 258 */           return empty();
/*     */         }
/*     */         
/*     */         private Iterator<XSComponent> ret(XSComponent one, Iterator<? extends XSComponent> rest) {
/* 262 */           return union(singleton(one), rest);
/*     */         }
/*     */       }
/*     */       
/*     */       public String toString() {
/* 267 */         return "/";
/*     */       }
/*     */     };
/*     */   
/* 271 */   public static final Axis<XSSchema> X_SCHEMA = new Axis<XSSchema>() {
/*     */       public Iterator<XSSchema> iterator(XSComponent contextNode) {
/* 273 */         return Iterators.singleton(contextNode.getOwnerSchema());
/*     */       }
/*     */       
/*     */       public Iterator<XSSchema> iterator(Iterator<? extends XSComponent> contextNodes) {
/* 277 */         return new Iterators.Adapter<XSSchema, XSComponent>(contextNodes) {
/*     */             protected XSSchema filter(XSComponent u) {
/* 279 */               return u.getOwnerSchema();
/*     */             }
/*     */           };
/*     */       }
/*     */       
/*     */       public boolean isModelGroup() {
/* 285 */         return false;
/*     */       }
/*     */       
/*     */       public String toString() {
/* 289 */         return "x-schema::";
/*     */       }
/*     */     };
/*     */   
/* 293 */   public static final Axis<XSElementDecl> SUBSTITUTION_GROUP = new AbstractAxisImpl<XSElementDecl>() {
/*     */       public Iterator<XSElementDecl> elementDecl(XSElementDecl decl) {
/* 295 */         return singleton(decl.getSubstAffiliation());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 299 */         return "substitutionGroup::";
/*     */       }
/*     */     };
/*     */   
/* 303 */   public static final Axis<XSAttributeDecl> ATTRIBUTE = new AbstractAxisImpl<XSAttributeDecl>() {
/*     */       public Iterator<XSAttributeDecl> complexType(XSComplexType type) {
/* 305 */         return attributeHolder((XSAttContainer)type);
/*     */       }
/*     */       
/*     */       public Iterator<XSAttributeDecl> attGroupDecl(XSAttGroupDecl decl) {
/* 309 */         return attributeHolder((XSAttContainer)decl);
/*     */       }
/*     */ 
/*     */       
/*     */       private Iterator<XSAttributeDecl> attributeHolder(XSAttContainer atts) {
/* 314 */         return new Iterators.Adapter<XSAttributeDecl, XSAttributeUse>(atts.iterateAttributeUses()) {
/*     */             protected XSAttributeDecl filter(XSAttributeUse u) {
/* 316 */               return u.getDecl();
/*     */             }
/*     */           };
/*     */       }
/*     */       
/*     */       public Iterator<XSAttributeDecl> schema(XSSchema schema) {
/* 322 */         return schema.iterateAttributeDecls();
/*     */       }
/*     */       
/*     */       public String toString() {
/* 326 */         return "@";
/*     */       }
/*     */     };
/*     */   
/* 330 */   public static final Axis<XSElementDecl> ELEMENT = new AbstractAxisImpl<XSElementDecl>() {
/*     */       public Iterator<XSElementDecl> particle(XSParticle particle) {
/* 332 */         return singleton(particle.getTerm().asElementDecl());
/*     */       }
/*     */       
/*     */       public Iterator<XSElementDecl> schema(XSSchema schema) {
/* 336 */         return schema.iterateElementDecls();
/*     */       }
/*     */       
/*     */       public Iterator<XSElementDecl> modelGroupDecl(XSModelGroupDecl decl) {
/* 340 */         return modelGroup(decl.getModelGroup());
/*     */       }
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
/*     */       public String getName() {
/* 353 */         return "";
/*     */       }
/*     */       
/*     */       public String toString() {
/* 357 */         return "element::";
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 362 */   public static final Axis<XSType> TYPE_DEFINITION = new AbstractAxisImpl<XSType>() {
/*     */       public Iterator<XSType> schema(XSSchema schema) {
/* 364 */         return schema.iterateTypes();
/*     */       }
/*     */       
/*     */       public Iterator<XSType> attributeDecl(XSAttributeDecl decl) {
/* 368 */         return singleton((XSType)decl.getType());
/*     */       }
/*     */       
/*     */       public Iterator<XSType> elementDecl(XSElementDecl decl) {
/* 372 */         return singleton(decl.getType());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 376 */         return "~";
/*     */       }
/*     */     };
/*     */   
/* 380 */   public static final Axis<XSType> BASETYPE = new AbstractAxisImpl<XSType>() {
/*     */       public Iterator<XSType> simpleType(XSSimpleType type) {
/* 382 */         return singleton(type.getBaseType());
/*     */       }
/*     */       
/*     */       public Iterator<XSType> complexType(XSComplexType type) {
/* 386 */         return singleton(type.getBaseType());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 390 */         return "baseType::";
/*     */       }
/*     */     };
/*     */   
/* 394 */   public static final Axis<XSSimpleType> PRIMITIVE_TYPE = new AbstractAxisImpl<XSSimpleType>() {
/*     */       public Iterator<XSSimpleType> simpleType(XSSimpleType type) {
/* 396 */         return singleton(type.getPrimitiveType());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 400 */         return "primitiveType::";
/*     */       }
/*     */     };
/*     */   
/* 404 */   public static final Axis<XSSimpleType> ITEM_TYPE = new AbstractAxisImpl<XSSimpleType>() {
/*     */       public Iterator<XSSimpleType> simpleType(XSSimpleType type) {
/* 406 */         XSListSimpleType baseList = type.getBaseListType();
/* 407 */         if (baseList == null) return empty(); 
/* 408 */         return singleton(baseList.getItemType());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 412 */         return "itemType::";
/*     */       }
/*     */     };
/*     */   
/* 416 */   public static final Axis<XSSimpleType> MEMBER_TYPE = new AbstractAxisImpl<XSSimpleType>() {
/*     */       public Iterator<XSSimpleType> simpleType(XSSimpleType type) {
/* 418 */         XSUnionSimpleType baseUnion = type.getBaseUnionType();
/* 419 */         if (baseUnion == null) return empty(); 
/* 420 */         return baseUnion.iterator();
/*     */       }
/*     */       
/*     */       public String toString() {
/* 424 */         return "memberType::";
/*     */       }
/*     */     };
/*     */   
/* 428 */   public static final Axis<XSComponent> SCOPE = new AbstractAxisImpl<XSComponent>() {
/*     */       public Iterator<XSComponent> complexType(XSComplexType type) {
/* 430 */         return singleton((XSComponent)type.getScope());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public String toString() {
/* 436 */         return "scope::";
/*     */       }
/*     */     };
/*     */   
/* 440 */   public static final Axis<XSAttGroupDecl> ATTRIBUTE_GROUP = new AbstractAxisImpl<XSAttGroupDecl>() {
/*     */       public Iterator<XSAttGroupDecl> schema(XSSchema schema) {
/* 442 */         return schema.iterateAttGroupDecls();
/*     */       }
/*     */       
/*     */       public String toString() {
/* 446 */         return "attributeGroup::";
/*     */       }
/*     */     };
/*     */   
/* 450 */   public static final Axis<XSModelGroupDecl> MODEL_GROUP_DECL = new AbstractAxisImpl<XSModelGroupDecl>() {
/*     */       public Iterator<XSModelGroupDecl> schema(XSSchema schema) {
/* 452 */         return schema.iterateModelGroupDecls();
/*     */       }
/*     */       
/*     */       public Iterator<XSModelGroupDecl> particle(XSParticle particle) {
/* 456 */         return singleton(particle.getTerm().asModelGroupDecl());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 460 */         return "group::";
/*     */       }
/*     */     };
/*     */   
/* 464 */   public static final Axis<XSIdentityConstraint> IDENTITY_CONSTRAINT = new AbstractAxisImpl<XSIdentityConstraint>() {
/*     */       public Iterator<XSIdentityConstraint> elementDecl(XSElementDecl decl) {
/* 466 */         return decl.getIdentityConstraints().iterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<XSIdentityConstraint> schema(XSSchema schema) {
/* 471 */         return super.schema(schema);
/*     */       }
/*     */       
/*     */       public String toString() {
/* 475 */         return "identityConstraint::";
/*     */       }
/*     */     };
/*     */   
/* 479 */   public static final Axis<XSIdentityConstraint> REFERENCED_KEY = new AbstractAxisImpl<XSIdentityConstraint>() {
/*     */       public Iterator<XSIdentityConstraint> identityConstraint(XSIdentityConstraint decl) {
/* 481 */         return singleton(decl.getReferencedKey());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 485 */         return "key::";
/*     */       }
/*     */     };
/*     */   
/* 489 */   public static final Axis<XSNotation> NOTATION = new AbstractAxisImpl<XSNotation>() {
/*     */       public Iterator<XSNotation> schema(XSSchema schema) {
/* 491 */         return schema.iterateNotations();
/*     */       }
/*     */       
/*     */       public String toString() {
/* 495 */         return "notation::";
/*     */       }
/*     */     };
/*     */   
/* 499 */   public static final Axis<XSWildcard> WILDCARD = new AbstractAxisImpl<XSWildcard>() {
/*     */       public Iterator<XSWildcard> particle(XSParticle particle) {
/* 501 */         return singleton(particle.getTerm().asWildcard());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 505 */         return "any::";
/*     */       }
/*     */     };
/*     */   
/* 509 */   public static final Axis<XSWildcard> ATTRIBUTE_WILDCARD = new AbstractAxisImpl<XSWildcard>() {
/*     */       public Iterator<XSWildcard> complexType(XSComplexType type) {
/* 511 */         return singleton(type.getAttributeWildcard());
/*     */       }
/*     */       
/*     */       public Iterator<XSWildcard> attGroupDecl(XSAttGroupDecl decl) {
/* 515 */         return singleton(decl.getAttributeWildcard());
/*     */       }
/*     */       
/*     */       public String toString() {
/* 519 */         return "anyAttribute::";
/*     */       }
/*     */     };
/*     */   
/* 523 */   public static final Axis<XSFacet> FACET = new AbstractAxisImpl<XSFacet>()
/*     */     {
/*     */       public Iterator<XSFacet> simpleType(XSSimpleType type) {
/* 526 */         XSRestrictionSimpleType r = type.asRestriction();
/* 527 */         if (r != null) {
/* 528 */           return r.iterateDeclaredFacets();
/*     */         }
/* 530 */         return empty();
/*     */       }
/*     */       
/*     */       public String toString() {
/* 534 */         return "facet::";
/*     */       }
/*     */     };
/*     */   
/* 538 */   public static final Axis<XSModelGroup> MODELGROUP_ALL = new ModelGroupAxis(XSModelGroup.Compositor.ALL);
/* 539 */   public static final Axis<XSModelGroup> MODELGROUP_CHOICE = new ModelGroupAxis(XSModelGroup.Compositor.CHOICE);
/* 540 */   public static final Axis<XSModelGroup> MODELGROUP_SEQUENCE = new ModelGroupAxis(XSModelGroup.Compositor.SEQUENCE); Iterator<T> iterator(XSComponent paramXSComponent); Iterator<T> iterator(Iterator<? extends XSComponent> paramIterator);
/* 541 */   public static final Axis<XSModelGroup> MODELGROUP_ANY = new ModelGroupAxis(null);
/*     */   
/*     */   boolean isModelGroup();
/*     */   
/*     */   public static final class ModelGroupAxis extends AbstractAxisImpl<XSModelGroup> {
/*     */     ModelGroupAxis(XSModelGroup.Compositor compositor) {
/* 547 */       this.compositor = compositor;
/*     */     }
/*     */     private final XSModelGroup.Compositor compositor;
/*     */     
/*     */     public boolean isModelGroup() {
/* 552 */       return true;
/*     */     }
/*     */     
/*     */     public Iterator<XSModelGroup> particle(XSParticle particle) {
/* 556 */       return filter(particle.getTerm().asModelGroup());
/*     */     }
/*     */     
/*     */     public Iterator<XSModelGroup> modelGroupDecl(XSModelGroupDecl decl) {
/* 560 */       return filter(decl.getModelGroup());
/*     */     }
/*     */     
/*     */     private Iterator<XSModelGroup> filter(XSModelGroup mg) {
/* 564 */       if (mg == null)
/* 565 */         return empty(); 
/* 566 */       if (mg.getCompositor() == this.compositor || this.compositor == null) {
/* 567 */         return singleton(mg);
/*     */       }
/* 569 */       return empty();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 573 */       if (this.compositor == null) {
/* 574 */         return "model::*";
/*     */       }
/* 576 */       return "model::" + this.compositor;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\scd\Axis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */