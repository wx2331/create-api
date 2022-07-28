/*     */ package com.sun.tools.internal.xjc.reader;
/*     */
/*     */ import com.sun.tools.internal.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CTypeRef;
/*     */ import com.sun.tools.internal.xjc.model.Multiplicity;
/*     */ import com.sun.tools.internal.xjc.model.nav.NType;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import java.math.BigInteger;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.activation.MimeType;
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
/*     */
/*     */
/*     */
/*     */ public final class RawTypeSet
/*     */ {
/*     */   public final Set<Ref> refs;
/*     */   public final Mode canBeTypeRefs;
/*     */   public final Multiplicity mul;
/*     */   private CElementPropertyInfo.CollectionMode collectionMode;
/*     */
/*     */   public RawTypeSet(Set<Ref> refs, Multiplicity m) {
/*  71 */     this.refs = refs;
/*  72 */     this.mul = m;
/*  73 */     this.canBeTypeRefs = canBeTypeRefs();
/*     */   }
/*     */
/*     */   public CElementPropertyInfo.CollectionMode getCollectionMode() {
/*  77 */     return this.collectionMode;
/*     */   }
/*     */
/*     */   public boolean isRequired() {
/*  81 */     return (this.mul.min.compareTo(BigInteger.ZERO) == 1);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public enum Mode
/*     */   {
/*  93 */     SHOULD_BE_TYPEREF(0),
/*     */
/*     */
/*     */
/*     */
/*  98 */     CAN_BE_TYPEREF(1),
/*     */
/*     */
/*     */
/* 102 */     MUST_BE_REFERENCE(2);
/*     */
/*     */     private final int rank;
/*     */
/*     */     Mode(int rank) {
/* 107 */       this.rank = rank;
/*     */     }
/*     */
/*     */     Mode or(Mode that) {
/* 111 */       switch (Math.max(this.rank, that.rank)) { case 0:
/* 112 */           return SHOULD_BE_TYPEREF;
/* 113 */         case 1: return CAN_BE_TYPEREF;
/* 114 */         case 2: return MUST_BE_REFERENCE; }
/*     */
/* 116 */       throw new AssertionError();
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
/*     */
/*     */   private Mode canBeTypeRefs() {
/* 132 */     Set<NType> types = new HashSet<>();
/*     */
/* 134 */     this.collectionMode = this.mul.isAtMostOnce() ? CElementPropertyInfo.CollectionMode.NOT_REPEATED : CElementPropertyInfo.CollectionMode.REPEATED_ELEMENT;
/*     */
/*     */
/*     */
/* 138 */     Mode mode = Mode.SHOULD_BE_TYPEREF;
/*     */
/* 140 */     for (Ref r : this.refs) {
/* 141 */       mode = mode.or(r.canBeType(this));
/* 142 */       if (mode == Mode.MUST_BE_REFERENCE) {
/* 143 */         return mode;
/*     */       }
/* 145 */       if (!types.add(r.toTypeRef(null).getTarget().getType()))
/* 146 */         return Mode.MUST_BE_REFERENCE;
/* 147 */       if (r.isListOfValues()) {
/* 148 */         if (this.refs.size() > 1 || !this.mul.isAtMostOnce())
/* 149 */           return Mode.MUST_BE_REFERENCE;
/* 150 */         this.collectionMode = CElementPropertyInfo.CollectionMode.REPEATED_VALUE;
/*     */       }
/*     */     }
/* 153 */     return mode;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void addTo(CElementPropertyInfo prop) {
/* 160 */     assert this.canBeTypeRefs != Mode.MUST_BE_REFERENCE;
/* 161 */     if (this.mul.isZero()) {
/*     */       return;
/*     */     }
/* 164 */     List<CTypeRef> dst = prop.getTypes();
/* 165 */     for (Ref t : this.refs)
/* 166 */       dst.add(t.toTypeRef(prop));
/*     */   }
/*     */
/*     */   public void addTo(CReferencePropertyInfo prop) {
/* 170 */     if (this.mul.isZero())
/*     */       return;
/* 172 */     for (Ref t : this.refs)
/* 173 */       t.toElementRef(prop);
/*     */   }
/*     */
/*     */   public ID id() {
/* 177 */     for (Ref t : this.refs) {
/* 178 */       ID id = t.id();
/* 179 */       if (id != ID.NONE) return id;
/*     */     }
/* 181 */     return ID.NONE;
/*     */   }
/*     */
/*     */   public MimeType getExpectedMimeType() {
/* 185 */     for (Ref t : this.refs) {
/* 186 */       MimeType mt = t.getExpectedMimeType();
/* 187 */       if (mt != null) return mt;
/*     */     }
/* 189 */     return null;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static abstract class Ref
/*     */   {
/*     */     protected abstract CTypeRef toTypeRef(CElementPropertyInfo param1CElementPropertyInfo);
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     protected abstract void toElementRef(CReferencePropertyInfo param1CReferencePropertyInfo);
/*     */
/*     */
/*     */
/*     */
/*     */     protected abstract Mode canBeType(RawTypeSet param1RawTypeSet);
/*     */
/*     */
/*     */
/*     */
/*     */     protected abstract boolean isListOfValues();
/*     */
/*     */
/*     */
/*     */
/*     */     protected abstract ID id();
/*     */
/*     */
/*     */
/*     */
/*     */     protected MimeType getExpectedMimeType() {
/* 225 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\RawTypeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
