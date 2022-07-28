/*     */ package com.sun.xml.internal.xsom.impl.util;
/*     */
/*     */ import com.sun.xml.internal.xsom.XSAnnotation;
/*     */ import com.sun.xml.internal.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.internal.xsom.XSAttributeUse;
/*     */ import com.sun.xml.internal.xsom.XSComplexType;
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
/*     */ import com.sun.xml.internal.xsom.XSSchemaSet;
/*     */ import com.sun.xml.internal.xsom.XSSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSType;
/*     */ import com.sun.xml.internal.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.internal.xsom.XSWildcard;
/*     */ import com.sun.xml.internal.xsom.XSXPath;
/*     */ import com.sun.xml.internal.xsom.visitor.XSSimpleTypeVisitor;
/*     */ import com.sun.xml.internal.xsom.visitor.XSTermVisitor;
/*     */ import com.sun.xml.internal.xsom.visitor.XSVisitor;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.math.BigInteger;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Iterator;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import javax.swing.tree.DefaultTreeModel;
/*     */ import javax.swing.tree.TreeCellRenderer;
/*     */ import org.xml.sax.Locator;
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
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class SchemaTreeTraverser
/*     */   implements XSVisitor, XSSimpleTypeVisitor
/*     */ {
/*     */   private SchemaTreeModel model;
/*     */   private SchemaTreeNode currNode;
/*     */
/*     */   public static final class SchemaTreeModel
/*     */     extends DefaultTreeModel
/*     */   {
/*     */     private SchemaTreeModel(SchemaRootNode root) {
/* 114 */       super(root);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public static SchemaTreeModel getInstance() {
/* 123 */       SchemaRootNode root = new SchemaRootNode();
/* 124 */       return new SchemaTreeModel(root);
/*     */     }
/*     */
/*     */     public void addSchemaNode(SchemaTreeNode node) {
/* 128 */       ((SchemaRootNode)this.root).add(node);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static class SchemaTreeNode
/*     */     extends DefaultMutableTreeNode
/*     */   {
/*     */     private String fileName;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private int lineNumber;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private String artifactName;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public SchemaTreeNode(String artifactName, Locator locator) {
/* 160 */       this.artifactName = artifactName;
/* 161 */       if (locator == null) {
/* 162 */         this.fileName = null;
/*     */       } else {
/*     */
/* 165 */         String filename = locator.getSystemId();
/* 166 */         filename = filename.replaceAll("%20", " ");
/*     */
/* 168 */         if (filename.startsWith("file:/")) {
/* 169 */           filename = filename.substring(6);
/*     */         }
/*     */
/* 172 */         this.fileName = filename;
/* 173 */         this.lineNumber = locator.getLineNumber() - 1;
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public String getCaption() {
/* 183 */       return this.artifactName;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public String getFileName() {
/* 190 */       return this.fileName;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public void setFileName(String fileName) {
/* 198 */       this.fileName = fileName;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public int getLineNumber() {
/* 206 */       return this.lineNumber;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public void setLineNumber(int lineNumber) {
/* 214 */       this.lineNumber = lineNumber;
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static class SchemaRootNode
/*     */     extends SchemaTreeNode
/*     */   {
/*     */     public SchemaRootNode() {
/* 228 */       super("Schema set", (Locator)null);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public static class SchemaTreeCellRenderer
/*     */     extends JPanel
/*     */     implements TreeCellRenderer
/*     */   {
/*     */     protected final JLabel iconLabel;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     protected final JLabel nameLabel;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     private boolean isSelected;
/*     */
/*     */
/*     */
/*     */
/* 258 */     public final Color selectedBackground = new Color(255, 244, 232);
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/* 265 */     public final Color selectedForeground = new Color(64, 32, 0);
/*     */
/*     */
/*     */
/*     */
/* 270 */     public final Font nameFont = new Font("Arial", 1, 12);
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public SchemaTreeCellRenderer() {
/* 277 */       FlowLayout fl = new FlowLayout(0, 1, 1);
/* 278 */       setLayout(fl);
/* 279 */       this.iconLabel = new JLabel();
/* 280 */       this.iconLabel.setOpaque(false);
/* 281 */       this.iconLabel.setBorder((Border)null);
/* 282 */       add(this.iconLabel);
/*     */
/*     */
/* 285 */       add(Box.createHorizontalStrut(5));
/*     */
/* 287 */       this.nameLabel = new JLabel();
/* 288 */       this.nameLabel.setOpaque(false);
/* 289 */       this.nameLabel.setBorder((Border)null);
/* 290 */       this.nameLabel.setFont(this.nameFont);
/* 291 */       add(this.nameLabel);
/*     */
/* 293 */       this.isSelected = false;
/*     */
/* 295 */       setOpaque(false);
/* 296 */       setBorder((Border)null);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public final void paintComponent(Graphics g) {
/* 305 */       int width = getWidth();
/* 306 */       int height = getHeight();
/* 307 */       if (this.isSelected) {
/* 308 */         g.setColor(this.selectedBackground);
/* 309 */         g.fillRect(0, 0, width - 1, height - 1);
/* 310 */         g.setColor(this.selectedForeground);
/* 311 */         g.drawRect(0, 0, width - 1, height - 1);
/*     */       }
/* 313 */       super.paintComponent(g);
/*     */     }
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
/*     */     protected final void setValues(Icon icon, String caption, boolean selected) {
/* 328 */       this.iconLabel.setIcon(icon);
/* 329 */       this.nameLabel.setText(caption);
/*     */
/* 331 */       this.isSelected = selected;
/* 332 */       if (selected) {
/* 333 */         this.nameLabel.setForeground(this.selectedForeground);
/*     */       } else {
/*     */
/* 336 */         this.nameLabel.setForeground(Color.black);
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public final Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
/* 346 */       if (value instanceof SchemaTreeNode) {
/* 347 */         SchemaTreeNode stn = (SchemaTreeNode)value;
/*     */
/* 349 */         setValues((Icon)null, stn.getCaption(), selected);
/* 350 */         return this;
/*     */       }
/* 352 */       throw new IllegalStateException("Unknown node");
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public SchemaTreeTraverser() {
/* 361 */     this.model = SchemaTreeModel.getInstance();
/* 362 */     this.currNode = (SchemaTreeNode)this.model.getRoot();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public SchemaTreeModel getModel() {
/* 371 */     return this.model;
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void visit(XSSchemaSet s) {
/* 380 */     for (XSSchema schema : s.getSchemas()) {
/* 381 */       schema(schema);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void schema(XSSchema s) {
/* 390 */     if (s.getTargetNamespace().equals("http://www.w3.org/2001/XMLSchema")) {
/*     */       return;
/*     */     }
/*     */
/*     */
/* 395 */     SchemaTreeNode newNode = new SchemaTreeNode("Schema " + s.getLocator().getSystemId(), s.getLocator());
/* 396 */     this.currNode = newNode;
/* 397 */     this.model.addSchemaNode(newNode);
/*     */
/* 399 */     for (XSAttGroupDecl groupDecl : s.getAttGroupDecls().values()) {
/* 400 */       attGroupDecl(groupDecl);
/*     */     }
/*     */
/* 403 */     for (XSAttributeDecl attrDecl : s.getAttributeDecls().values()) {
/* 404 */       attributeDecl(attrDecl);
/*     */     }
/*     */
/* 407 */     for (XSComplexType complexType : s.getComplexTypes().values()) {
/* 408 */       complexType(complexType);
/*     */     }
/*     */
/* 411 */     for (XSElementDecl elementDecl : s.getElementDecls().values()) {
/* 412 */       elementDecl(elementDecl);
/*     */     }
/*     */
/* 415 */     for (XSModelGroupDecl modelGroupDecl : s.getModelGroupDecls().values()) {
/* 416 */       modelGroupDecl(modelGroupDecl);
/*     */     }
/*     */
/* 419 */     for (XSSimpleType simpleType : s.getSimpleTypes().values()) {
/* 420 */       simpleType(simpleType);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void attGroupDecl(XSAttGroupDecl decl) {
/* 429 */     SchemaTreeNode newNode = new SchemaTreeNode("Attribute group \"" + decl.getName() + "\"", decl.getLocator());
/* 430 */     this.currNode.add(newNode);
/* 431 */     this.currNode = newNode;
/*     */
/*     */
/*     */
/* 435 */     Iterator<XSAttGroupDecl> itr = decl.iterateAttGroups();
/* 436 */     while (itr.hasNext()) {
/* 437 */       dumpRef(itr.next());
/*     */     }
/*     */
/* 440 */     itr = decl.iterateDeclaredAttributeUses();
/* 441 */     while (itr.hasNext()) {
/* 442 */       attributeUse((XSAttributeUse)itr.next());
/*     */     }
/*     */
/* 445 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void dumpRef(XSAttGroupDecl decl) {
/* 456 */     SchemaTreeNode newNode = new SchemaTreeNode("Attribute group ref \"{" + decl.getTargetNamespace() + "}" + decl.getName() + "\"", decl.getLocator());
/* 457 */     this.currNode.add(newNode);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void attributeUse(XSAttributeUse use) {
/* 464 */     XSAttributeDecl decl = use.getDecl();
/*     */
/* 466 */     String additionalAtts = "";
/*     */
/* 468 */     if (use.isRequired()) {
/* 469 */       additionalAtts = additionalAtts + " use=\"required\"";
/*     */     }
/* 471 */     if (use.getFixedValue() != null && use
/* 472 */       .getDecl().getFixedValue() == null) {
/* 473 */       additionalAtts = additionalAtts + " fixed=\"" + use.getFixedValue() + "\"";
/*     */     }
/* 475 */     if (use.getDefaultValue() != null && use
/* 476 */       .getDecl().getDefaultValue() == null) {
/* 477 */       additionalAtts = additionalAtts + " default=\"" + use.getDefaultValue() + "\"";
/*     */     }
/*     */
/* 480 */     if (decl.isLocal()) {
/*     */
/* 482 */       dump(decl, additionalAtts);
/*     */     }
/*     */     else {
/*     */
/* 486 */       String str = MessageFormat.format("Attribute ref \"'{'{0}'}'{1}{2}\"", new Object[] { decl
/*     */
/* 488 */             .getTargetNamespace(), decl.getName(), additionalAtts });
/*     */
/* 490 */       SchemaTreeNode newNode = new SchemaTreeNode(str, decl.getLocator());
/* 491 */       this.currNode.add(newNode);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void attributeDecl(XSAttributeDecl decl) {
/* 499 */     dump(decl, "");
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private void dump(XSAttributeDecl decl, String additionalAtts) {
/* 509 */     XSSimpleType type = decl.getType();
/*     */
/* 511 */     String str = MessageFormat.format("Attribute \"{0}\"{1}{2}{3}{4}", new Object[] { decl
/*     */
/* 513 */           .getName(), additionalAtts,
/*     */
/* 515 */           type.isLocal() ? "" : MessageFormat.format(" type=\"'{'{0}'}'{1}\"", new Object[] { type
/*     */
/* 517 */               .getTargetNamespace(), type
/* 518 */               .getName()
/* 519 */             }), (decl.getFixedValue() == null) ? "" : (" fixed=\"" + decl
/* 520 */           .getFixedValue() + "\""),
/* 521 */           (decl.getDefaultValue() == null) ? "" : (" default=\"" + decl
/* 522 */           .getDefaultValue() + "\"") });
/*     */
/* 524 */     SchemaTreeNode newNode = new SchemaTreeNode(str, decl.getLocator());
/* 525 */     this.currNode.add(newNode);
/* 526 */     this.currNode = newNode;
/*     */
/* 528 */     if (type.isLocal()) {
/* 529 */       simpleType(type);
/*     */     }
/* 531 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void simpleType(XSSimpleType type) {
/* 539 */     String str = MessageFormat.format("Simple type {0}", new Object[] {
/* 540 */           type.isLocal() ? "" : (" name=\"" + type
/* 541 */           .getName() + "\"")
/*     */         });
/* 543 */     SchemaTreeNode newNode = new SchemaTreeNode(str, type.getLocator());
/* 544 */     this.currNode.add(newNode);
/* 545 */     this.currNode = newNode;
/*     */
/* 547 */     type.visit(this);
/*     */
/* 549 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void listSimpleType(XSListSimpleType type) {
/* 556 */     XSSimpleType itemType = type.getItemType();
/*     */
/* 558 */     if (itemType.isLocal()) {
/*     */
/* 560 */       SchemaTreeNode newNode = new SchemaTreeNode("List", type.getLocator());
/* 561 */       this.currNode.add(newNode);
/* 562 */       this.currNode = newNode;
/* 563 */       simpleType(itemType);
/* 564 */       this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */     }
/*     */     else {
/*     */
/* 568 */       String str = MessageFormat.format("List itemType=\"'{'{0}'}'{1}\"", new Object[] { itemType
/* 569 */             .getTargetNamespace(), itemType
/* 570 */             .getName() });
/*     */
/* 572 */       SchemaTreeNode newNode = new SchemaTreeNode(str, itemType.getLocator());
/* 573 */       this.currNode.add(newNode);
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void unionSimpleType(XSUnionSimpleType type) {
/* 581 */     int len = type.getMemberSize();
/* 582 */     StringBuffer ref = new StringBuffer();
/*     */
/* 584 */     for (int i = 0; i < len; i++) {
/* 585 */       XSSimpleType member = type.getMember(i);
/* 586 */       if (member.isGlobal()) {
/* 587 */         ref.append(MessageFormat.format(" '{'{0}'}'{1}", new Object[] { member
/*     */
/* 589 */                 .getTargetNamespace(), member
/* 590 */                 .getName() }));
/*     */       }
/*     */     }
/*     */
/* 594 */     String name = (ref.length() == 0) ? "Union" : ("Union memberTypes=\"" + ref + "\"");
/*     */
/* 596 */     SchemaTreeNode newNode = new SchemaTreeNode(name, type.getLocator());
/* 597 */     this.currNode.add(newNode);
/* 598 */     this.currNode = newNode;
/*     */
/* 600 */     for (int j = 0; j < len; j++) {
/* 601 */       XSSimpleType member = type.getMember(j);
/* 602 */       if (member.isLocal()) {
/* 603 */         simpleType(member);
/*     */       }
/*     */     }
/* 606 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void restrictionSimpleType(XSRestrictionSimpleType type) {
/* 614 */     if (type.getBaseType() == null) {
/*     */
/* 616 */       if (!type.getName().equals("anySimpleType")) {
/* 617 */         throw new InternalError();
/*     */       }
/* 619 */       if (!"http://www.w3.org/2001/XMLSchema".equals(type.getTargetNamespace())) {
/* 620 */         throw new InternalError();
/*     */       }
/*     */
/*     */       return;
/*     */     }
/* 625 */     XSSimpleType baseType = type.getSimpleBaseType();
/*     */
/* 627 */     String str = MessageFormat.format("Restriction {0}", new Object[] {
/* 628 */           baseType.isLocal() ? "" : (" base=\"{" + baseType
/* 629 */           .getTargetNamespace() + "}" + baseType
/* 630 */           .getName() + "\"")
/*     */         });
/* 632 */     SchemaTreeNode newNode = new SchemaTreeNode(str, baseType.getLocator());
/* 633 */     this.currNode.add(newNode);
/* 634 */     this.currNode = newNode;
/*     */
/* 636 */     if (baseType.isLocal()) {
/* 637 */       simpleType(baseType);
/*     */     }
/*     */
/* 640 */     Iterator<XSFacet> itr = type.iterateDeclaredFacets();
/* 641 */     while (itr.hasNext()) {
/* 642 */       facet(itr.next());
/*     */     }
/*     */
/* 645 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void facet(XSFacet facet) {
/* 655 */     SchemaTreeNode newNode = new SchemaTreeNode(MessageFormat.format("{0} value=\"{1}\"", new Object[] { facet.getName(), facet.getValue() }), facet.getLocator());
/* 656 */     this.currNode.add(newNode);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void notation(XSNotation notation) {
/* 666 */     SchemaTreeNode newNode = new SchemaTreeNode(MessageFormat.format("Notation name='\"0}\" public =\"{1}\" system=\"{2}\"", new Object[] { notation.getName(), notation.getPublicId(), notation.getSystemId() }), notation.getLocator());
/* 667 */     this.currNode.add(newNode);
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void complexType(XSComplexType type) {
/* 677 */     SchemaTreeNode newNode = new SchemaTreeNode(MessageFormat.format("ComplexType {0}", new Object[] { type.isLocal() ? "" : (" name=\"" + type.getName() + "\"") }), type.getLocator());
/* 678 */     this.currNode.add(newNode);
/* 679 */     this.currNode = newNode;
/*     */
/*     */
/*     */
/* 683 */     if (type.getContentType().asSimpleType() != null) {
/*     */
/*     */
/* 686 */       SchemaTreeNode newNode2 = new SchemaTreeNode("Simple content", type.getContentType().getLocator());
/* 687 */       this.currNode.add(newNode2);
/* 688 */       this.currNode = newNode2;
/*     */
/* 690 */       XSType baseType = type.getBaseType();
/*     */
/* 692 */       if (type.getDerivationMethod() == 2) {
/*     */
/* 694 */         String str = MessageFormat.format("Restriction base=\"<{0}>{1}\"", new Object[] { baseType
/*     */
/* 696 */               .getTargetNamespace(), baseType
/* 697 */               .getName() });
/*     */
/* 699 */         SchemaTreeNode newNode3 = new SchemaTreeNode(str, baseType.getLocator());
/* 700 */         this.currNode.add(newNode3);
/* 701 */         this.currNode = newNode3;
/*     */
/* 703 */         dumpComplexTypeAttribute(type);
/*     */
/* 705 */         this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */       }
/*     */       else {
/*     */
/* 709 */         String str = MessageFormat.format("Extension base=\"<{0}>{1}\"", new Object[] { baseType
/*     */
/* 711 */               .getTargetNamespace(), baseType
/* 712 */               .getName() });
/*     */
/* 714 */         SchemaTreeNode newNode3 = new SchemaTreeNode(str, baseType.getLocator());
/* 715 */         this.currNode.add(newNode3);
/* 716 */         this.currNode = newNode3;
/*     */
/*     */
/* 719 */         if (type.getTargetNamespace().compareTo(baseType
/* 720 */             .getTargetNamespace()) == 0 && type
/*     */
/* 722 */           .getName().compareTo(baseType.getName()) == 0) {
/*     */
/*     */
/* 725 */           SchemaTreeNode newNodeRedefine = new SchemaTreeNode("redefine", type.getLocator());
/* 726 */           this.currNode.add(newNodeRedefine);
/* 727 */           this.currNode = newNodeRedefine;
/* 728 */           baseType.visit(this);
/* 729 */           this
/* 730 */             .currNode = (SchemaTreeNode)newNodeRedefine.getParent();
/*     */         }
/*     */
/* 733 */         dumpComplexTypeAttribute(type);
/*     */
/* 735 */         this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */       }
/*     */
/* 738 */       this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */
/*     */     }
/*     */     else {
/*     */
/* 743 */       SchemaTreeNode newNode2 = new SchemaTreeNode("Complex content", type.getContentType().getLocator());
/* 744 */       this.currNode.add(newNode2);
/* 745 */       this.currNode = newNode2;
/*     */
/* 747 */       XSComplexType baseType = type.getBaseType().asComplexType();
/*     */
/* 749 */       if (type.getDerivationMethod() == 2) {
/*     */
/* 751 */         String str = MessageFormat.format("Restriction base=\"<{0}>{1}\"", new Object[] { baseType
/*     */
/* 753 */               .getTargetNamespace(), baseType
/* 754 */               .getName() });
/*     */
/* 756 */         SchemaTreeNode newNode3 = new SchemaTreeNode(str, baseType.getLocator());
/* 757 */         this.currNode.add(newNode3);
/* 758 */         this.currNode = newNode3;
/*     */
/* 760 */         type.getContentType().visit(this);
/* 761 */         dumpComplexTypeAttribute(type);
/*     */
/* 763 */         this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */       }
/*     */       else {
/*     */
/* 767 */         String str = MessageFormat.format("Extension base=\"'{'{0}'}'{1}\"", new Object[] { baseType
/*     */
/* 769 */               .getTargetNamespace(), baseType
/* 770 */               .getName() });
/*     */
/* 772 */         SchemaTreeNode newNode3 = new SchemaTreeNode(str, baseType.getLocator());
/* 773 */         this.currNode.add(newNode3);
/* 774 */         this.currNode = newNode3;
/*     */
/*     */
/* 777 */         if (type.getTargetNamespace().compareTo(baseType
/* 778 */             .getTargetNamespace()) == 0 && type
/*     */
/* 780 */           .getName().compareTo(baseType.getName()) == 0) {
/*     */
/*     */
/* 783 */           SchemaTreeNode newNodeRedefine = new SchemaTreeNode("redefine", type.getLocator());
/* 784 */           this.currNode.add(newNodeRedefine);
/* 785 */           this.currNode = newNodeRedefine;
/* 786 */           baseType.visit(this);
/* 787 */           this
/* 788 */             .currNode = (SchemaTreeNode)newNodeRedefine.getParent();
/*     */         }
/*     */
/* 791 */         type.getExplicitContent().visit(this);
/* 792 */         dumpComplexTypeAttribute(type);
/*     */
/* 794 */         this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */       }
/*     */
/* 797 */       this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */     }
/*     */
/* 800 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private void dumpComplexTypeAttribute(XSComplexType type) {
/* 811 */     Iterator<XSAttGroupDecl> itr = type.iterateAttGroups();
/* 812 */     while (itr.hasNext()) {
/* 813 */       dumpRef(itr.next());
/*     */     }
/*     */
/* 816 */     itr = type.iterateDeclaredAttributeUses();
/* 817 */     while (itr.hasNext()) {
/* 818 */       attributeUse((XSAttributeUse)itr.next());
/*     */     }
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void elementDecl(XSElementDecl decl) {
/* 826 */     elementDecl(decl, "");
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   private void elementDecl(XSElementDecl decl, String extraAtts) {
/* 836 */     XSType type = decl.getType();
/*     */
/*     */
/*     */
/* 840 */     String str = MessageFormat.format("Element name=\"{0}\"{1}{2}", new Object[] { decl
/*     */
/* 842 */           .getName(),
/* 843 */           type.isLocal() ? "" : (" type=\"{" + type
/* 844 */           .getTargetNamespace() + "}" + type
/* 845 */           .getName() + "\""), extraAtts });
/*     */
/* 847 */     SchemaTreeNode newNode = new SchemaTreeNode(str, decl.getLocator());
/* 848 */     this.currNode.add(newNode);
/* 849 */     this.currNode = newNode;
/*     */
/* 851 */     if (type.isLocal() &&
/* 852 */       type.isLocal()) {
/* 853 */       type.visit(this);
/*     */     }
/*     */
/*     */
/* 857 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void modelGroupDecl(XSModelGroupDecl decl) {
/* 866 */     SchemaTreeNode newNode = new SchemaTreeNode(MessageFormat.format("Group name=\"{0}\"", new Object[] { decl.getName() }), decl.getLocator());
/* 867 */     this.currNode.add(newNode);
/* 868 */     this.currNode = newNode;
/*     */
/* 870 */     modelGroup(decl.getModelGroup());
/*     */
/* 872 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void modelGroup(XSModelGroup group) {
/* 879 */     modelGroup(group, "");
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
/*     */   private void modelGroup(XSModelGroup group, String extraAtts) {
/* 891 */     SchemaTreeNode newNode = new SchemaTreeNode(MessageFormat.format("{0}{1}", new Object[] { group.getCompositor(), extraAtts }), group.getLocator());
/* 892 */     this.currNode.add(newNode);
/* 893 */     this.currNode = newNode;
/*     */
/* 895 */     int len = group.getSize();
/* 896 */     for (int i = 0; i < len; i++) {
/* 897 */       particle(group.getChild(i));
/*     */     }
/*     */
/* 900 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   public void particle(XSParticle part) {
/* 909 */     StringBuffer buf = new StringBuffer();
/*     */
/* 911 */     BigInteger i = part.getMaxOccurs();
/* 912 */     if (i.equals(BigInteger.valueOf(-1L))) {
/* 913 */       buf.append(" maxOccurs=\"unbounded\"");
/*     */
/*     */     }
/* 916 */     else if (!i.equals(BigInteger.ONE)) {
/* 917 */       buf.append(" maxOccurs=\"" + i + "\"");
/*     */     }
/*     */
/*     */
/* 921 */     i = part.getMinOccurs();
/* 922 */     if (!i.equals(BigInteger.ONE)) {
/* 923 */       buf.append(" minOccurs=\"" + i + "\"");
/*     */     }
/*     */
/* 926 */     final String extraAtts = buf.toString();
/*     */
/* 928 */     part.getTerm().visit(new XSTermVisitor() {
/*     */           public void elementDecl(XSElementDecl decl) {
/* 930 */             if (decl.isLocal()) {
/* 931 */               SchemaTreeTraverser.this.elementDecl(decl, extraAtts);
/*     */
/*     */
/*     */             }
/*     */             else {
/*     */
/*     */
/*     */
/* 939 */               SchemaTreeNode newNode = new SchemaTreeNode(MessageFormat.format("Element ref=\"'{'{0}'}'{1}\"{2}", new Object[] { decl.getTargetNamespace(), decl.getName(), this.val$extraAtts }), decl.getLocator());
/* 940 */               SchemaTreeTraverser.this.currNode.add(newNode);
/*     */             }
/*     */           }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */           public void modelGroupDecl(XSModelGroupDecl decl) {
/* 949 */             SchemaTreeNode newNode = new SchemaTreeNode(MessageFormat.format("Group ref=\"'{'{0}'}'{1}\"{2}", new Object[] { decl.getTargetNamespace(), decl.getName(), this.val$extraAtts }), decl.getLocator());
/* 950 */             SchemaTreeTraverser.this.currNode.add(newNode);
/*     */           }
/*     */
/*     */           public void modelGroup(XSModelGroup group) {
/* 954 */             SchemaTreeTraverser.this.modelGroup(group, extraAtts);
/*     */           }
/*     */
/*     */           public void wildcard(XSWildcard wc) {
/* 958 */             SchemaTreeTraverser.this.wildcard(wc, extraAtts);
/*     */           }
/*     */         });
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */   public void wildcard(XSWildcard wc) {
/* 967 */     wildcard(wc, "");
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
/*     */   private void wildcard(XSWildcard wc, String extraAtts) {
/* 979 */     SchemaTreeNode newNode = new SchemaTreeNode(MessageFormat.format("Any ", new Object[] { extraAtts }), wc.getLocator());
/* 980 */     this.currNode.add(newNode);
/*     */   }
/*     */
/*     */   public void annotation(XSAnnotation ann) {}
/*     */
/*     */   public void empty(XSContentType t) {}
/*     */
/*     */   public void identityConstraint(XSIdentityConstraint ic) {}
/*     */
/*     */   public void xpath(XSXPath xp) {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\imp\\util\SchemaTreeTraverser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
