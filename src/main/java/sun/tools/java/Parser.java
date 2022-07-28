/*      */ package sun.tools.java;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.util.Vector;
/*      */ import sun.tools.tree.AddExpression;
/*      */ import sun.tools.tree.AndExpression;
/*      */ import sun.tools.tree.ArrayAccessExpression;
/*      */ import sun.tools.tree.AssignAddExpression;
/*      */ import sun.tools.tree.AssignBitAndExpression;
/*      */ import sun.tools.tree.AssignBitOrExpression;
/*      */ import sun.tools.tree.AssignBitXorExpression;
/*      */ import sun.tools.tree.AssignDivideExpression;
/*      */ import sun.tools.tree.AssignExpression;
/*      */ import sun.tools.tree.AssignMultiplyExpression;
/*      */ import sun.tools.tree.AssignRemainderExpression;
/*      */ import sun.tools.tree.AssignShiftLeftExpression;
/*      */ import sun.tools.tree.AssignShiftRightExpression;
/*      */ import sun.tools.tree.AssignSubtractExpression;
/*      */ import sun.tools.tree.AssignUnsignedShiftRightExpression;
/*      */ import sun.tools.tree.BitAndExpression;
/*      */ import sun.tools.tree.BitOrExpression;
/*      */ import sun.tools.tree.BitXorExpression;
/*      */ import sun.tools.tree.BooleanExpression;
/*      */ import sun.tools.tree.CaseStatement;
/*      */ import sun.tools.tree.CommaExpression;
/*      */ import sun.tools.tree.CompoundStatement;
/*      */ import sun.tools.tree.ConditionalExpression;
/*      */ import sun.tools.tree.DivideExpression;
/*      */ import sun.tools.tree.DoubleExpression;
/*      */ import sun.tools.tree.EqualExpression;
/*      */ import sun.tools.tree.Expression;
/*      */ import sun.tools.tree.ExpressionStatement;
/*      */ import sun.tools.tree.FieldExpression;
/*      */ import sun.tools.tree.FloatExpression;
/*      */ import sun.tools.tree.GreaterExpression;
/*      */ import sun.tools.tree.GreaterOrEqualExpression;
/*      */ import sun.tools.tree.IdentifierExpression;
/*      */ import sun.tools.tree.InstanceOfExpression;
/*      */ import sun.tools.tree.IntExpression;
/*      */ import sun.tools.tree.LessExpression;
/*      */ import sun.tools.tree.LessOrEqualExpression;
/*      */ import sun.tools.tree.LongExpression;
/*      */ import sun.tools.tree.MultiplyExpression;
/*      */ import sun.tools.tree.NewArrayExpression;
/*      */ import sun.tools.tree.Node;
/*      */ import sun.tools.tree.NotEqualExpression;
/*      */ import sun.tools.tree.OrExpression;
/*      */ import sun.tools.tree.PostDecExpression;
/*      */ import sun.tools.tree.PostIncExpression;
/*      */ import sun.tools.tree.RemainderExpression;
/*      */ import sun.tools.tree.ShiftLeftExpression;
/*      */ import sun.tools.tree.ShiftRightExpression;
/*      */ import sun.tools.tree.Statement;
/*      */ import sun.tools.tree.SubtractExpression;
/*      */ import sun.tools.tree.SuperExpression;
/*      */ import sun.tools.tree.ThisExpression;
/*      */ import sun.tools.tree.TryStatement;
/*      */ import sun.tools.tree.TypeExpression;
/*      */ import sun.tools.tree.UnsignedShiftRightExpression;
/*      */ import sun.tools.tree.VarDeclarationStatement;
/*      */ 
/*      */ public class Parser extends Scanner implements ParserActions, Constants {
/*      */   ParserActions actions;
/*      */   private Node[] args;
/*      */   protected int argIndex;
/*      */   private int aCount;
/*      */   private Type[] aTypes;
/*      */   private IdentifierToken[] aNames;
/*      */   private ClassDefinition curClass;
/*      */   private int FPstate;
/*      */   protected Scanner scanner;
/*      */   
/*   73 */   protected Parser(Environment paramEnvironment, InputStream paramInputStream) throws IOException { super(paramEnvironment, paramInputStream);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  274 */     this.args = new Node[32];
/*  275 */     this.argIndex = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1505 */     this.aCount = 0;
/* 1506 */     this.aTypes = new Type[8];
/* 1507 */     this.aNames = new IdentifierToken[this.aTypes.length];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1842 */     this.FPstate = 0; this.scanner = this; this.actions = this; } protected Parser(Scanner paramScanner) throws IOException { super(paramScanner.env); this.args = new Node[32]; this.argIndex = 0; this.aCount = 0; this.aTypes = new Type[8]; this.aNames = new IdentifierToken[this.aTypes.length]; this.FPstate = 0; this.scanner = paramScanner; this.env = paramScanner.env; this.token = paramScanner.token; this.pos = paramScanner.pos; this.actions = this; }
/*      */   public Parser(Scanner paramScanner, ParserActions paramParserActions) throws IOException { this(paramScanner); this.actions = paramParserActions; }
/*      */   @Deprecated public void packageDeclaration(long paramLong, IdentifierToken paramIdentifierToken) { packageDeclaration(paramLong, paramIdentifierToken.id); }
/*      */   @Deprecated protected void packageDeclaration(long paramLong, Identifier paramIdentifier) { throw new RuntimeException("beginClass method is abstract"); }
/*      */   @Deprecated public void importClass(long paramLong, IdentifierToken paramIdentifierToken) { importClass(paramLong, paramIdentifierToken.id); }
/*      */   @Deprecated protected void importClass(long paramLong, Identifier paramIdentifier) { throw new RuntimeException("importClass method is abstract"); }
/* 1848 */   @Deprecated public void importPackage(long paramLong, IdentifierToken paramIdentifierToken) { importPackage(paramLong, paramIdentifierToken.id); } @Deprecated protected void importPackage(long paramLong, Identifier paramIdentifier) { throw new RuntimeException("importPackage method is abstract"); } @Deprecated public ClassDefinition beginClass(long paramLong, String paramString, int paramInt, IdentifierToken paramIdentifierToken1, IdentifierToken paramIdentifierToken2, IdentifierToken[] paramArrayOfIdentifierToken) { Identifier identifier = (paramIdentifierToken2 == null) ? null : paramIdentifierToken2.id; Identifier[] arrayOfIdentifier = null; if (paramArrayOfIdentifierToken != null) { arrayOfIdentifier = new Identifier[paramArrayOfIdentifierToken.length]; for (byte b = 0; b < paramArrayOfIdentifierToken.length; b++) arrayOfIdentifier[b] = (paramArrayOfIdentifierToken[b]).id;  }  beginClass(paramLong, paramString, paramInt, paramIdentifierToken1.id, identifier, arrayOfIdentifier); return getCurrentClass(); } @Deprecated protected void beginClass(long paramLong, String paramString, int paramInt, Identifier paramIdentifier1, Identifier paramIdentifier2, Identifier[] paramArrayOfIdentifier) { throw new RuntimeException("beginClass method is abstract"); } protected ClassDefinition getCurrentClass() { return null; } @Deprecated public void endClass(long paramLong, ClassDefinition paramClassDefinition) { endClass(paramLong, paramClassDefinition.getName().getFlatName().getName()); } @Deprecated protected void endClass(long paramLong, Identifier paramIdentifier) { throw new RuntimeException("endClass method is abstract"); } @Deprecated public void defineField(long paramLong, ClassDefinition paramClassDefinition, String paramString, int paramInt, Type paramType, IdentifierToken paramIdentifierToken, IdentifierToken[] paramArrayOfIdentifierToken1, IdentifierToken[] paramArrayOfIdentifierToken2, Node paramNode) { Identifier[] arrayOfIdentifier1 = null; Identifier[] arrayOfIdentifier2 = null; if (paramArrayOfIdentifierToken1 != null) { arrayOfIdentifier1 = new Identifier[paramArrayOfIdentifierToken1.length]; for (byte b = 0; b < paramArrayOfIdentifierToken1.length; b++) arrayOfIdentifier1[b] = (paramArrayOfIdentifierToken1[b]).id;  }  if (paramArrayOfIdentifierToken2 != null) { arrayOfIdentifier2 = new Identifier[paramArrayOfIdentifierToken2.length]; for (byte b = 0; b < paramArrayOfIdentifierToken2.length; b++) arrayOfIdentifier2[b] = (paramArrayOfIdentifierToken2[b]).id;  }  defineField(paramLong, paramString, paramInt, paramType, paramIdentifierToken.id, arrayOfIdentifier1, arrayOfIdentifier2, paramNode); } @Deprecated protected void defineField(long paramLong, String paramString, int paramInt, Type paramType, Identifier paramIdentifier, Identifier[] paramArrayOfIdentifier1, Identifier[] paramArrayOfIdentifier2, Node paramNode) { throw new RuntimeException("defineField method is abstract"); } protected final void addArgument(Node paramNode) { if (this.argIndex == this.args.length) { Node[] arrayOfNode = new Node[this.args.length * 2]; System.arraycopy(this.args, 0, arrayOfNode, 0, this.args.length); this.args = arrayOfNode; }  this.args[this.argIndex++] = paramNode; } protected final Expression[] exprArgs(int paramInt) { Expression[] arrayOfExpression = new Expression[this.argIndex - paramInt]; System.arraycopy(this.args, paramInt, arrayOfExpression, 0, this.argIndex - paramInt); this.argIndex = paramInt; return arrayOfExpression; } protected final Statement[] statArgs(int paramInt) { Statement[] arrayOfStatement = new Statement[this.argIndex - paramInt]; System.arraycopy(this.args, paramInt, arrayOfStatement, 0, this.argIndex - paramInt); this.argIndex = paramInt; return arrayOfStatement; } protected void expect(int paramInt) throws SyntaxError, IOException { if (this.token != paramInt) { switch (paramInt) { case 60: this.env.error(this.scanner.prevPos, "identifier.expected"); throw new SyntaxError(); }  this.env.error(this.scanner.prevPos, "token.expected", opNames[paramInt]); throw new SyntaxError(); }  scan(); } protected Expression parseTypeExpression() throws SyntaxError, IOException { IdentifierExpression identifierExpression; FieldExpression fieldExpression; switch (this.token) { case 77: return (Expression)new TypeExpression(scan(), Type.tVoid);case 78: return (Expression)new TypeExpression(scan(), Type.tBoolean);case 70: return (Expression)new TypeExpression(scan(), Type.tByte);case 71: return (Expression)new TypeExpression(scan(), Type.tChar);case 72: return (Expression)new TypeExpression(scan(), Type.tShort);case 73: return (Expression)new TypeExpression(scan(), Type.tInt);case 74: return (Expression)new TypeExpression(scan(), Type.tLong);case 75: return (Expression)new TypeExpression(scan(), Type.tFloat);case 76: return (Expression)new TypeExpression(scan(), Type.tDouble);case 60: identifierExpression = new IdentifierExpression(this.pos, this.scanner.idValue); scan(); while (this.token == 46) { fieldExpression = new FieldExpression(scan(), (Expression)identifierExpression, this.scanner.idValue); expect(60); }  return (Expression)fieldExpression; }  this.env.error(this.pos, "type.expected"); throw new SyntaxError(); } protected Expression parseMethodExpression(Expression paramExpression, Identifier paramIdentifier) throws SyntaxError, IOException { long l = scan(); int i = this.argIndex; if (this.token != 141) { addArgument((Node)parseExpression()); while (this.token == 0) { scan(); addArgument((Node)parseExpression()); }  }  expect(141); return (Expression)new MethodExpression(l, paramExpression, paramIdentifier, exprArgs(i)); } protected Expression parseNewInstanceExpression(long paramLong, Expression paramExpression1, Expression paramExpression2) throws SyntaxError, IOException { int i = this.argIndex; expect(140); if (this.token != 141) { addArgument((Node)parseExpression()); while (this.token == 0) { scan(); addArgument((Node)parseExpression()); }  }  expect(141); ClassDefinition classDefinition = null; if (this.token == 138 && !(paramExpression2 instanceof TypeExpression)) { long l = this.pos; Identifier identifier = FieldExpression.toIdentifier(paramExpression2); if (identifier == null) this.env.error(paramExpression2.getWhere(), "type.expected");  Vector<IdentifierToken> vector = new Vector(1); Vector vector1 = new Vector(0); vector.addElement(new IdentifierToken(idNull)); if (this.token == 113 || this.token == 112) { this.env.error(this.pos, "anonymous.extends"); parseInheritance(vector, vector1); }  classDefinition = parseClassBody(new IdentifierToken(l, idNull), 196608, 56, (String)null, vector, vector1, paramExpression2.getWhere()); }  if (paramExpression1 == null && classDefinition == null) return (Expression)new NewInstanceExpression(paramLong, paramExpression2, exprArgs(i));  return (Expression)new NewInstanceExpression(paramLong, paramExpression2, exprArgs(i), paramExpression1, classDefinition); } protected Expression parseTerm() throws SyntaxError, IOException { char c; int i; long l2; float f1; double d1; String str; Identifier identifier; ThisExpression thisExpression; SuperExpression superExpression; long l1, l3, l6; int m; long l5; float f3; double d3; int k; long l4; float f2; double d2; Expression expression1; int j; long l7; Expression expression2; long l8; switch (this.token) { case 63: c = this.scanner.charValue; return (Expression)new CharExpression(scan(), c);case 65: i = this.scanner.intValue; l3 = scan(); if (i < 0 && this.radix == 10) this.env.error(l3, "overflow.int.dec");  return (Expression)new IntExpression(l3, i);case 66: l2 = this.scanner.longValue; l6 = scan(); if (l2 < 0L && this.radix == 10) this.env.error(l6, "overflow.long.dec");  return (Expression)new LongExpression(l6, l2);case 67: f1 = this.scanner.floatValue; return (Expression)new FloatExpression(scan(), f1);case 68: d1 = this.scanner.doubleValue; return (Expression)new DoubleExpression(scan(), d1);case 69: str = this.scanner.stringValue; return (Expression)new StringExpression(scan(), str);case 60: identifier = this.scanner.idValue; l3 = scan(); return (this.token == 140) ? parseMethodExpression((Expression)null, identifier) : (Expression)new IdentifierExpression(l3, identifier);case 80: return (Expression)new BooleanExpression(scan(), true);case 81: return (Expression)new BooleanExpression(scan(), false);case 84: return (Expression)new NullExpression(scan());case 82: thisExpression = new ThisExpression(scan()); return (this.token == 140) ? parseMethodExpression((Expression)thisExpression, idInit) : (Expression)thisExpression;case 83: superExpression = new SuperExpression(scan()); return (this.token == 140) ? parseMethodExpression((Expression)superExpression, idInit) : (Expression)superExpression;case 70: case 71: case 72: case 73: case 74: case 75: case 76: case 77: case 78: return parseTypeExpression();case 29: l1 = scan(); switch (this.token) { case 65: m = this.scanner.intValue; l7 = scan(); if (m < 0 && this.radix == 10) this.env.error(l7, "overflow.int.dec");  return (Expression)new IntExpression(l7, m);case 66: l5 = this.scanner.longValue; l8 = scan(); if (l5 < 0L && this.radix == 10) this.env.error(l8, "overflow.long.dec");  return (Expression)new LongExpression(l8, l5);case 67: f3 = this.scanner.floatValue; return (Expression)new FloatExpression(scan(), f3);case 68: d3 = this.scanner.doubleValue; return (Expression)new DoubleExpression(scan(), d3); }  return (Expression)new PositiveExpression(l1, parseTerm());case 30: l1 = scan(); switch (this.token) { case 65: k = -this.scanner.intValue; return (Expression)new IntExpression(scan(), k);case 66: l4 = -this.scanner.longValue; return (Expression)new LongExpression(scan(), l4);case 67: f2 = -this.scanner.floatValue; return (Expression)new FloatExpression(scan(), f2);case 68: d2 = -this.scanner.doubleValue; return (Expression)new DoubleExpression(scan(), d2); }  return (Expression)new NegativeExpression(l1, parseTerm());case 37: return (Expression)new NotExpression(scan(), parseTerm());case 38: return (Expression)new BitNotExpression(scan(), parseTerm());case 50: return (Expression)new PreIncExpression(scan(), parseTerm());case 51: return (Expression)new PreDecExpression(scan(), parseTerm());case 140: l1 = scan(); expression1 = parseExpression(); expect(141); if (expression1.getOp() == 147) return (Expression)new CastExpression(l1, expression1, parseTerm());  switch (this.token) { case 50: return (Expression)new PostIncExpression(scan(), expression1);case 51: return (Expression)new PostDecExpression(scan(), expression1);case 37: case 38: case 49: case 60: case 63: case 65: case 66: case 67: case 68: case 69: case 80: case 81: case 82: case 83: case 84: case 140: return (Expression)new CastExpression(l1, expression1, parseTerm()); }  return (Expression)new ExprExpression(l1, expression1);case 138: l1 = scan(); j = this.argIndex; if (this.token != 139) { addArgument((Node)parseExpression()); while (this.token == 0) { scan(); if (this.token == 139) break;  addArgument((Node)parseExpression()); }  }  expect(139); return (Expression)new ArrayExpression(l1, exprArgs(j));case 49: l1 = scan(); j = this.argIndex; if (this.token == 140) { scan(); Expression expression = parseExpression(); expect(141); this.env.error(l1, "not.supported", "new(...)"); return (Expression)new NullExpression(l1); }  expression2 = parseTypeExpression(); if (this.token == 142) { while (this.token == 142) { scan(); addArgument((this.token != 143) ? (Node)parseExpression() : null); expect(143); }  Expression[] arrayOfExpression = exprArgs(j); if (this.token == 138) return (Expression)new NewArrayExpression(l1, expression2, arrayOfExpression, parseTerm());  return (Expression)new NewArrayExpression(l1, expression2, arrayOfExpression); }  return parseNewInstanceExpression(l1, (Expression)null, expression2); }  this.env.error(this.scanner.prevPos, "missing.term"); return (Expression)new IntExpression(this.pos, 0); } protected Expression parseExpression() throws SyntaxError, IOException { for (Expression expression = parseTerm(); expression != null; expression = expression.order()) { Expression expression1 = parseBinaryExpression(expression); if (expression1 == null) return expression;  expression = expression1; }  return null; } protected Expression parseBinaryExpression(Expression paramExpression) throws SyntaxError, IOException { ConditionalExpression conditionalExpression; if (paramExpression != null) { ArrayAccessExpression arrayAccessExpression; PostIncExpression postIncExpression; PostDecExpression postDecExpression; FieldExpression fieldExpression; InstanceOfExpression instanceOfExpression; AddExpression addExpression; SubtractExpression subtractExpression; MultiplyExpression multiplyExpression; DivideExpression divideExpression; RemainderExpression remainderExpression; ShiftLeftExpression shiftLeftExpression; ShiftRightExpression shiftRightExpression; UnsignedShiftRightExpression unsignedShiftRightExpression; LessExpression lessExpression; LessOrEqualExpression lessOrEqualExpression; GreaterExpression greaterExpression; GreaterOrEqualExpression greaterOrEqualExpression; EqualExpression equalExpression; NotEqualExpression notEqualExpression; BitAndExpression bitAndExpression; BitXorExpression bitXorExpression; BitOrExpression bitOrExpression; AndExpression andExpression; OrExpression orExpression; AssignExpression assignExpression; AssignMultiplyExpression assignMultiplyExpression; AssignDivideExpression assignDivideExpression; AssignRemainderExpression assignRemainderExpression; AssignAddExpression assignAddExpression; AssignSubtractExpression assignSubtractExpression; AssignShiftLeftExpression assignShiftLeftExpression; AssignShiftRightExpression assignShiftRightExpression; AssignUnsignedShiftRightExpression assignUnsignedShiftRightExpression; AssignBitAndExpression assignBitAndExpression; AssignBitOrExpression assignBitOrExpression; AssignBitXorExpression assignBitXorExpression; long l; Expression expression1; Expression expression2; switch (this.token) { case 142: l = scan(); expression1 = (this.token != 143) ? parseExpression() : null; expect(143); arrayAccessExpression = new ArrayAccessExpression(l, paramExpression, expression1); return (Expression)arrayAccessExpression;case 50: postIncExpression = new PostIncExpression(scan(), (Expression)arrayAccessExpression); return (Expression)postIncExpression;case 51: postDecExpression = new PostDecExpression(scan(), (Expression)postIncExpression); return (Expression)postDecExpression;case 46: l = scan(); if (this.token == 82) { Expression expression; long l1 = scan(); if (this.token == 140) { ThisExpression thisExpression = new ThisExpression(l1, (Expression)postDecExpression); expression = parseMethodExpression((Expression)thisExpression, idInit); } else { fieldExpression = new FieldExpression(l, expression, idThis); }  } else { FieldExpression fieldExpression1; if (this.token == 83) { Expression expression; long l1 = scan(); if (this.token == 140) { SuperExpression superExpression = new SuperExpression(l1, (Expression)fieldExpression); expression = parseMethodExpression((Expression)superExpression, idInit); } else { fieldExpression1 = new FieldExpression(l, expression, idSuper); }  } else { Expression expression; if (this.token == 49) { scan(); if (this.token != 60) expect(60);  expression = parseNewInstanceExpression(l, (Expression)fieldExpression1, parseTypeExpression()); } else { FieldExpression fieldExpression2; if (this.token == 111) { scan(); fieldExpression2 = new FieldExpression(l, expression, idClass); } else { Expression expression3; Identifier identifier = this.scanner.idValue; expect(60); if (this.token == 140) { expression3 = parseMethodExpression((Expression)fieldExpression2, identifier); } else { fieldExpression = new FieldExpression(l, expression3, identifier); }  }  }  }  }  return (Expression)fieldExpression;case 25: instanceOfExpression = new InstanceOfExpression(scan(), (Expression)fieldExpression, parseTerm()); return (Expression)instanceOfExpression;case 29: addExpression = new AddExpression(scan(), (Expression)instanceOfExpression, parseTerm()); return (Expression)addExpression;case 30: subtractExpression = new SubtractExpression(scan(), (Expression)addExpression, parseTerm()); return (Expression)subtractExpression;case 33: multiplyExpression = new MultiplyExpression(scan(), (Expression)subtractExpression, parseTerm()); return (Expression)multiplyExpression;case 31: divideExpression = new DivideExpression(scan(), (Expression)multiplyExpression, parseTerm()); return (Expression)divideExpression;case 32: remainderExpression = new RemainderExpression(scan(), (Expression)divideExpression, parseTerm()); return (Expression)remainderExpression;case 26: shiftLeftExpression = new ShiftLeftExpression(scan(), (Expression)remainderExpression, parseTerm()); return (Expression)shiftLeftExpression;case 27: shiftRightExpression = new ShiftRightExpression(scan(), (Expression)shiftLeftExpression, parseTerm()); return (Expression)shiftRightExpression;case 28: unsignedShiftRightExpression = new UnsignedShiftRightExpression(scan(), (Expression)shiftRightExpression, parseTerm()); return (Expression)unsignedShiftRightExpression;case 24: lessExpression = new LessExpression(scan(), (Expression)unsignedShiftRightExpression, parseTerm()); return (Expression)lessExpression;case 23: lessOrEqualExpression = new LessOrEqualExpression(scan(), (Expression)lessExpression, parseTerm()); return (Expression)lessOrEqualExpression;case 22: greaterExpression = new GreaterExpression(scan(), (Expression)lessOrEqualExpression, parseTerm()); return (Expression)greaterExpression;case 21: greaterOrEqualExpression = new GreaterOrEqualExpression(scan(), (Expression)greaterExpression, parseTerm()); return (Expression)greaterOrEqualExpression;case 20: equalExpression = new EqualExpression(scan(), (Expression)greaterOrEqualExpression, parseTerm()); return (Expression)equalExpression;case 19: notEqualExpression = new NotEqualExpression(scan(), (Expression)equalExpression, parseTerm()); return (Expression)notEqualExpression;case 18: bitAndExpression = new BitAndExpression(scan(), (Expression)notEqualExpression, parseTerm()); return (Expression)bitAndExpression;case 17: bitXorExpression = new BitXorExpression(scan(), (Expression)bitAndExpression, parseTerm()); return (Expression)bitXorExpression;case 16: bitOrExpression = new BitOrExpression(scan(), (Expression)bitXorExpression, parseTerm()); return (Expression)bitOrExpression;case 15: andExpression = new AndExpression(scan(), (Expression)bitOrExpression, parseTerm()); return (Expression)andExpression;case 14: orExpression = new OrExpression(scan(), (Expression)andExpression, parseTerm()); return (Expression)orExpression;case 1: assignExpression = new AssignExpression(scan(), (Expression)orExpression, parseTerm()); return (Expression)assignExpression;case 2: assignMultiplyExpression = new AssignMultiplyExpression(scan(), (Expression)assignExpression, parseTerm()); return (Expression)assignMultiplyExpression;case 3: assignDivideExpression = new AssignDivideExpression(scan(), (Expression)assignMultiplyExpression, parseTerm()); return (Expression)assignDivideExpression;case 4: assignRemainderExpression = new AssignRemainderExpression(scan(), (Expression)assignDivideExpression, parseTerm()); return (Expression)assignRemainderExpression;case 5: assignAddExpression = new AssignAddExpression(scan(), (Expression)assignRemainderExpression, parseTerm()); return (Expression)assignAddExpression;case 6: assignSubtractExpression = new AssignSubtractExpression(scan(), (Expression)assignAddExpression, parseTerm()); return (Expression)assignSubtractExpression;case 7: assignShiftLeftExpression = new AssignShiftLeftExpression(scan(), (Expression)assignSubtractExpression, parseTerm()); return (Expression)assignShiftLeftExpression;case 8: assignShiftRightExpression = new AssignShiftRightExpression(scan(), (Expression)assignShiftLeftExpression, parseTerm()); return (Expression)assignShiftRightExpression;case 9: assignUnsignedShiftRightExpression = new AssignUnsignedShiftRightExpression(scan(), (Expression)assignShiftRightExpression, parseTerm()); return (Expression)assignUnsignedShiftRightExpression;case 10: assignBitAndExpression = new AssignBitAndExpression(scan(), (Expression)assignUnsignedShiftRightExpression, parseTerm()); return (Expression)assignBitAndExpression;case 11: assignBitOrExpression = new AssignBitOrExpression(scan(), (Expression)assignBitAndExpression, parseTerm()); return (Expression)assignBitOrExpression;case 12: assignBitXorExpression = new AssignBitXorExpression(scan(), (Expression)assignBitOrExpression, parseTerm()); return (Expression)assignBitXorExpression;case 137: l = scan(); expression1 = parseExpression(); expect(136); expression2 = parseExpression(); if (expression2 instanceof AssignExpression || expression2 instanceof sun.tools.tree.AssignOpExpression) this.env.error(expression2.getWhere(), "assign.in.conditionalexpr");  conditionalExpression = new ConditionalExpression(l, (Expression)assignBitXorExpression, expression1, expression2); return (Expression)conditionalExpression; }  return null; }  return (Expression)conditionalExpression; } protected Statement parseLocalClass(int paramInt) throws SyntaxError, IOException { long l = this.pos;
/* 1849 */     ClassDefinition classDefinition = parseNamedClass(0x20000 | paramInt, 105, (String)null);
/* 1850 */     Statement[] arrayOfStatement = { (Statement)new VarDeclarationStatement(l, new LocalMember(classDefinition), null) };
/*      */ 
/*      */     
/* 1853 */     TypeExpression typeExpression = new TypeExpression(l, classDefinition.getType());
/* 1854 */     return (Statement)new DeclarationStatement(l, 0, (Expression)typeExpression, arrayOfStatement); }
/*      */   protected boolean recoverStatement() throws SyntaxError, IOException { while (true) { switch (this.token) { case -1: case 90: case 92: case 93: case 94: case 98: case 99: case 100: case 101: case 102: case 103: case 138: case 139: return true;case 77: case 111: case 114: case 120: case 121: case 124: case 125: case 126: expect(139); return false;case 140: match(140, 141); scan(); continue;case 142: match(142, 143); scan(); continue; }  scan(); }  }
/*      */   protected Statement parseDeclaration(long paramLong, int paramInt, Expression paramExpression) throws SyntaxError, IOException { int i = this.argIndex; if (this.token == 60) { addArgument((Node)new VarDeclarationStatement(this.pos, parseExpression())); while (this.token == 0) { scan(); addArgument((Node)new VarDeclarationStatement(this.pos, parseExpression())); }  }  return (Statement)new DeclarationStatement(paramLong, paramInt, paramExpression, statArgs(i)); }
/*      */   protected void topLevelExpression(Expression paramExpression) { switch (paramExpression.getOp()) { case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11: case 12: case 39: case 40: case 42: case 44: case 45: case 47: return; }  this.env.error(paramExpression.getWhere(), "invalid.expr"); }
/*      */   protected Statement parseStatement() throws SyntaxError, IOException { long l2; Statement statement1; Expression expression5; ExpressionStatement expressionStatement; Expression expression4; Statement statement2; Identifier identifier; Expression expression3; int j; Expression expression2; int i; Statement statement3; Expression expression7; int k; Expression expression6, expression8; CommaExpression commaExpression; boolean bool; Statement statement4, statement5; TryStatement tryStatement; switch (this.token) { case 135: return (Statement)new CompoundStatement(scan(), new Statement[0]);case 138: return parseBlockStatement();case 90: l2 = scan(); expect(140); expression5 = parseExpression(); expect(141); statement3 = parseStatement(); if (this.token == 91) { scan(); return (Statement)new IfStatement(l2, expression5, statement3, parseStatement()); }  return (Statement)new IfStatement(l2, expression5, statement3, null);case 91: this.env.error(scan(), "else.without.if"); return parseStatement();case 92: l2 = scan(); expression5 = null; statement3 = null; expression8 = null; expect(140); if (this.token != 135) { long l = this.pos; int m = parseModifiers(16); Expression expression = parseExpression(); if (this.token == 60) { Statement statement = parseDeclaration(l, m, expression); } else { CommaExpression commaExpression1; if (m != 0) expect(60);  topLevelExpression(expression); while (this.token == 0) { long l3 = scan(); Expression expression9 = parseExpression(); topLevelExpression(expression9); commaExpression1 = new CommaExpression(l3, expression, expression9); }  expressionStatement = new ExpressionStatement(l, (Expression)commaExpression1); }  }  expect(135); if (this.token != 135)
/*      */           expression7 = parseExpression();  expect(135); if (this.token != 141) { expression8 = parseExpression(); topLevelExpression(expression8); while (this.token == 0) { long l = scan(); Expression expression = parseExpression(); topLevelExpression(expression); commaExpression = new CommaExpression(l, expression8, expression); }  }  expect(141); return (Statement)new ForStatement(l2, (Statement)expressionStatement, expression7, (Expression)commaExpression, parseStatement());case 93: l2 = scan(); expect(140); expression4 = parseExpression(); expect(141); return (Statement)new WhileStatement(l2, expression4, parseStatement());case 94: l2 = scan(); statement2 = parseStatement(); expect(93); expect(140); expression7 = parseExpression(); expect(141); expect(135); return (Statement)new DoStatement(l2, statement2, expression7);case 98: l2 = scan(); statement2 = null; if (this.token == 60) { identifier = this.scanner.idValue; scan(); }  expect(135); return (Statement)new BreakStatement(l2, identifier);case 99: l2 = scan(); identifier = null; if (this.token == 60) { identifier = this.scanner.idValue; scan(); }  expect(135); return (Statement)new ContinueStatement(l2, identifier);case 100: l2 = scan(); identifier = null; if (this.token != 135)
/*      */           expression3 = parseExpression();  expect(135); return (Statement)new ReturnStatement(l2, expression3);case 95: l2 = scan(); j = this.argIndex; expect(140); expression7 = parseExpression(); expect(141); expect(138); while (this.token != -1 && this.token != 139) { int m = this.argIndex; try { switch (this.token) { case 96: addArgument((Node)new CaseStatement(scan(), parseExpression())); expect(136); continue;case 97: addArgument((Node)new CaseStatement(scan(), null)); expect(136); continue; }  addArgument((Node)parseStatement()); } catch (SyntaxError syntaxError) { this.argIndex = m; if (!recoverStatement())
/*      */               throw syntaxError;  }  }  expect(139); return (Statement)new SwitchStatement(l2, expression7, statArgs(j));case 96: this.env.error(this.pos, "case.without.switch"); while (this.token == 96) { scan(); parseExpression(); expect(136); }  return parseStatement();case 97: this.env.error(this.pos, "default.without.switch"); scan(); expect(136); return parseStatement();case 101: l2 = scan(); expression2 = null; k = this.argIndex; bool = false; statement5 = parseBlockStatement(); if (expression2 != null); while (this.token == 102) { long l = this.pos; expect(102); expect(140); int m = parseModifiers(16); Expression expression = parseExpression(); IdentifierToken identifierToken = this.scanner.getIdToken(); expect(60); identifierToken.modifiers = m; expect(141); addArgument((Node)new CatchStatement(l, expression, identifierToken, parseBlockStatement())); bool = true; }  if (bool)
/*      */           tryStatement = new TryStatement(l2, statement5, statArgs(k));  if (this.token == 103) { scan(); return (Statement)new FinallyStatement(l2, (Statement)tryStatement, parseBlockStatement()); }  if (bool || expression2 != null)
/*      */           return (Statement)tryStatement;  this.env.error(this.pos, "try.without.catch.finally"); return (Statement)new TryStatement(l2, (Statement)tryStatement, null);case 102: this.env.error(this.pos, "catch.without.try"); do { scan(); expect(140); parseModifiers(16); parseExpression(); expect(60); expect(141); statement1 = parseBlockStatement(); } while (this.token == 102); if (this.token == 103) { scan(); statement1 = parseBlockStatement(); }  return statement1;case 103: this.env.error(this.pos, "finally.without.try"); scan(); return parseBlockStatement();case 104: l1 = scan(); expression2 = parseExpression(); expect(135); return (Statement)new ThrowStatement(l1, expression2);case 58: l1 = scan(); expect(60); expect(135); this.env.error(l1, "not.supported", "goto"); return (Statement)new CompoundStatement(l1, new Statement[0]);case 126: l1 = scan(); expect(140); expression2 = parseExpression(); expect(141); return (Statement)new SynchronizedStatement(l1, expression2, parseBlockStatement());case 111: case 114: return parseLocalClass(0);case 123: case 128: case 130: case 131: l1 = this.pos; i = parseModifiers(2098192); switch (this.token) { case 111: case 114: return parseLocalClass(i);case 60: case 70: case 71: case 72: case 73: case 74: case 75: case 76: case 78: if ((i & 0x200400) != 0) { i &= 0xFFDFFBFF; expect(111); }  expression6 = parseExpression(); if (this.token != 60)
/* 1864 */               expect(60);  statement4 = parseDeclaration(l1, i, expression6); expect(135); return statement4; }  this.env.error(this.pos, "type.expected"); throw new SyntaxError();case 77: case 120: case 121: case 124: case 125: this.env.error(this.pos, "statement.expected"); throw new SyntaxError(); }  long l1 = this.pos; Expression expression1 = parseExpression(); if (this.token == 60) { Statement statement = parseDeclaration(l1, 0, expression1); expect(135); return statement; }  if (this.token == 136) { scan(); Statement statement = parseStatement(); statement.setLabel(this.env, expression1); return statement; }  topLevelExpression(expression1); expect(135); return (Statement)new ExpressionStatement(l1, expression1); } protected ClassDefinition parseNamedClass(int paramInt1, int paramInt2, String paramString) throws SyntaxError, IOException { switch (this.token) {
/*      */       case 114:
/* 1866 */         scan();
/* 1867 */         paramInt1 |= 0x200;
/*      */         break;
/*      */       
/*      */       case 111:
/* 1871 */         scan();
/*      */         break;
/*      */       
/*      */       default:
/* 1875 */         this.env.error(this.pos, "class.expected");
/*      */         break;
/*      */     } 
/*      */     
/* 1879 */     int i = this.FPstate;
/* 1880 */     if ((paramInt1 & 0x200000) != 0) {
/* 1881 */       this.FPstate = 2097152;
/*      */     }
/*      */     else {
/*      */       
/* 1885 */       paramInt1 |= this.FPstate & 0x200000;
/*      */     } 
/*      */ 
/*      */     
/* 1889 */     IdentifierToken identifierToken = this.scanner.getIdToken();
/* 1890 */     long l = this.pos;
/* 1891 */     expect(60);
/*      */     
/* 1893 */     Vector vector1 = new Vector();
/* 1894 */     Vector vector2 = new Vector();
/* 1895 */     parseInheritance(vector1, vector2);
/*      */     
/* 1897 */     ClassDefinition classDefinition = parseClassBody(identifierToken, paramInt1, paramInt2, paramString, vector1, vector2, l);
/*      */     
/* 1899 */     this.FPstate = i;
/*      */     
/* 1901 */     return classDefinition; } protected Statement parseBlockStatement() throws SyntaxError, IOException { if (this.token != 138) { this.env.error(this.scanner.prevPos, "token.expected", opNames[138]); return parseStatement(); }  long l = scan(); int i = this.argIndex; while (this.token != -1 && this.token != 139) { int j = this.argIndex; try { addArgument((Node)parseStatement()); } catch (SyntaxError syntaxError) { this.argIndex = j; if (!recoverStatement()) throw syntaxError;  }  }  expect(139); return (Statement)new CompoundStatement(l, statArgs(i)); } protected IdentifierToken parseName(boolean paramBoolean) throws SyntaxError, IOException { IdentifierToken identifierToken = this.scanner.getIdToken(); expect(60); if (this.token != 46) return identifierToken;  StringBuffer stringBuffer = new StringBuffer(identifierToken.id.toString()); while (this.token == 46) { scan(); if (this.token == 33 && paramBoolean) { scan(); stringBuffer.append(".*"); break; }  stringBuffer.append('.'); if (this.token == 60) stringBuffer.append(this.scanner.idValue);  expect(60); }  identifierToken.id = Identifier.lookup(stringBuffer.toString()); return identifierToken; } @Deprecated protected Identifier parseIdentifier(boolean paramBoolean) throws SyntaxError, IOException { return (parseName(paramBoolean)).id; } protected Type parseType() throws SyntaxError, IOException { Type type; switch (this.token) { case 60: type = Type.tClass((parseName(false)).id); return parseArrayBrackets(type);case 77: scan(); type = Type.tVoid; return parseArrayBrackets(type);case 78: scan(); type = Type.tBoolean; return parseArrayBrackets(type);case 70: scan(); type = Type.tByte; return parseArrayBrackets(type);case 71: scan(); type = Type.tChar; return parseArrayBrackets(type);case 72: scan(); type = Type.tShort; return parseArrayBrackets(type);case 73: scan(); type = Type.tInt; return parseArrayBrackets(type);case 75: scan(); type = Type.tFloat; return parseArrayBrackets(type);case 74: scan(); type = Type.tLong; return parseArrayBrackets(type);case 76: scan(); type = Type.tDouble; return parseArrayBrackets(type); }  this.env.error(this.pos, "type.expected"); throw new SyntaxError(); } protected Type parseArrayBrackets(Type paramType) throws SyntaxError, IOException { while (this.token == 142) { scan(); if (this.token != 143) { this.env.error(this.pos, "array.dim.in.decl"); parseExpression(); }  expect(143); paramType = Type.tArray(paramType); }  return paramType; } private void addArgument(int paramInt, Type paramType, IdentifierToken paramIdentifierToken) { paramIdentifierToken.modifiers = paramInt; if (this.aCount >= this.aTypes.length) { Type[] arrayOfType = new Type[this.aCount * 2]; System.arraycopy(this.aTypes, 0, arrayOfType, 0, this.aCount); this.aTypes = arrayOfType; IdentifierToken[] arrayOfIdentifierToken = new IdentifierToken[this.aCount * 2]; System.arraycopy(this.aNames, 0, arrayOfIdentifierToken, 0, this.aCount); this.aNames = arrayOfIdentifierToken; }  this.aTypes[this.aCount] = paramType; this.aNames[this.aCount++] = paramIdentifierToken; }
/*      */   protected int parseModifiers(int paramInt) throws IOException { int i = 0; while (true) { if (this.token == 123) { this.env.error(this.pos, "not.supported", "const"); scan(); }  int j = 0; switch (this.token) { case 120: j = 2; break;case 121: j = 1; break;case 122: j = 4; break;case 124: j = 8; break;case 125: j = 128; break;case 128: j = 16; break;case 130: j = 1024; break;case 127: j = 256; break;case 129: j = 64; break;case 126: j = 32; break;case 131: j = 2097152; break; }  if ((j & paramInt) == 0) break;  if ((j & i) != 0) this.env.error(this.pos, "repeated.modifier");  i |= j; scan(); }  return i; }
/*      */   protected void parseField() throws SyntaxError, IOException { if (this.token == 135) { scan(); return; }  String str = this.scanner.docComment; long l = this.pos; int i = parseModifiers(2098687); if (i == (i & 0x8) && this.token == 138) { this.actions.defineField(l, this.curClass, str, i, Type.tMethod(Type.tVoid), new IdentifierToken(idClassInit), null, null, (Node)parseStatement()); return; }  if (this.token == 111 || this.token == 114) { parseNamedClass(i, 111, str); return; }  l = this.pos; Type type = parseType(); IdentifierToken identifierToken = null; switch (this.token) { case 60: identifierToken = this.scanner.getIdToken(); l = scan(); break;case 140: identifierToken = new IdentifierToken(idInit); if ((i & 0x200000) != 0) this.env.error(this.pos, "bad.constructor.modifier");  break;default: expect(60); break; }  if (this.token == 140) { int j; scan(); this.aCount = 0; if (this.token != 141) { int k = parseModifiers(16); Type type1 = parseType(); IdentifierToken identifierToken1 = this.scanner.getIdToken(); expect(60); type1 = parseArrayBrackets(type1); addArgument(k, type1, identifierToken1); while (this.token == 0) { scan(); k = parseModifiers(16); type1 = parseType(); identifierToken1 = this.scanner.getIdToken(); expect(60); type1 = parseArrayBrackets(type1); addArgument(k, type1, identifierToken1); }  }  expect(141); type = parseArrayBrackets(type); Type[] arrayOfType = new Type[this.aCount]; System.arraycopy(this.aTypes, 0, arrayOfType, 0, this.aCount); IdentifierToken[] arrayOfIdentifierToken1 = new IdentifierToken[this.aCount]; System.arraycopy(this.aNames, 0, arrayOfIdentifierToken1, 0, this.aCount); type = Type.tMethod(type, arrayOfType); IdentifierToken[] arrayOfIdentifierToken2 = null; if (this.token == 144) { Vector<IdentifierToken> vector = new Vector(); scan(); vector.addElement(parseName(false)); while (this.token == 0) { scan(); vector.addElement(parseName(false)); }  arrayOfIdentifierToken2 = new IdentifierToken[vector.size()]; vector.copyInto((Object[])arrayOfIdentifierToken2); }  switch (this.token) { case 138: j = this.FPstate; if ((i & 0x200000) != 0) { this.FPstate = 2097152; } else { i |= this.FPstate & 0x200000; }  this.actions.defineField(l, this.curClass, str, i, type, identifierToken, arrayOfIdentifierToken1, arrayOfIdentifierToken2, (Node)parseStatement()); this.FPstate = j; return;case 135: scan(); this.actions.defineField(l, this.curClass, str, i, type, identifierToken, arrayOfIdentifierToken1, arrayOfIdentifierToken2, null); return; }  if ((i & 0x500) == 0) { expect(138); } else { expect(135); }  return; }  while (true) { l = this.pos; Type type1 = parseArrayBrackets(type); Expression expression = null; if (this.token == 1) { scan(); expression = parseExpression(); }  this.actions.defineField(l, this.curClass, str, i, type1, identifierToken, null, null, (Node)expression); if (this.token != 0) { expect(135); return; }  scan(); identifierToken = this.scanner.getIdToken(); expect(60); }  }
/*      */   protected void recoverField(ClassDefinition paramClassDefinition) throws SyntaxError, IOException { while (true) { switch (this.token) { case -1: case 70: case 71: case 72: case 73: case 74: case 75: case 76: case 77: case 78: case 120: case 121: case 124: case 125: case 126: case 128: return;case 138: match(138, 139); scan(); continue;case 140: match(140, 141); scan(); continue;case 142: match(142, 143); scan(); continue;case 110: case 111: case 114: case 115: case 139: this.actions.endClass(this.pos, paramClassDefinition); throw new SyntaxError(); }  scan(); }  }
/*      */   protected void parseClass() throws SyntaxError, IOException { String str = this.scanner.docComment; int i = parseModifiers(2098719); parseNamedClass(i, 115, str); }
/* 1906 */   protected void parseInheritance(Vector<IdentifierToken> paramVector1, Vector<IdentifierToken> paramVector2) throws SyntaxError, IOException { if (this.token == 112) {
/* 1907 */       scan();
/* 1908 */       paramVector1.addElement(parseName(false));
/* 1909 */       while (this.token == 0) {
/* 1910 */         scan();
/* 1911 */         paramVector1.addElement(parseName(false));
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1916 */     if (this.token == 113) {
/* 1917 */       scan();
/* 1918 */       paramVector2.addElement(parseName(false));
/* 1919 */       while (this.token == 0) {
/* 1920 */         scan();
/* 1921 */         paramVector2.addElement(parseName(false));
/*      */       } 
/*      */     }  }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ClassDefinition parseClassBody(IdentifierToken paramIdentifierToken, int paramInt1, int paramInt2, String paramString, Vector<IdentifierToken> paramVector1, Vector<IdentifierToken> paramVector2, long paramLong) throws SyntaxError, IOException {
/* 1935 */     IdentifierToken identifierToken = null;
/* 1936 */     if ((paramInt1 & 0x200) != 0) {
/* 1937 */       if (paramVector2.size() > 0) {
/* 1938 */         this.env.error(((IdentifierToken)paramVector2.elementAt(0)).getWhere(), "intf.impl.intf");
/*      */       }
/*      */       
/* 1941 */       paramVector2 = paramVector1;
/*      */     }
/* 1943 */     else if (paramVector1.size() > 0) {
/* 1944 */       if (paramVector1.size() > 1) {
/* 1945 */         this.env.error(((IdentifierToken)paramVector1.elementAt(1)).getWhere(), "multiple.inherit");
/*      */       }
/*      */       
/* 1948 */       identifierToken = paramVector1.elementAt(0);
/*      */     } 
/*      */ 
/*      */     
/* 1952 */     ClassDefinition classDefinition1 = this.curClass;
/*      */ 
/*      */     
/* 1955 */     IdentifierToken[] arrayOfIdentifierToken = new IdentifierToken[paramVector2.size()];
/* 1956 */     paramVector2.copyInto((Object[])arrayOfIdentifierToken);
/*      */     
/* 1958 */     ClassDefinition classDefinition2 = this.actions.beginClass(paramLong, paramString, paramInt1, paramIdentifierToken, identifierToken, arrayOfIdentifierToken);
/*      */ 
/*      */     
/* 1961 */     expect(138);
/* 1962 */     while (this.token != -1 && this.token != 139) {
/*      */       try {
/* 1964 */         this.curClass = classDefinition2;
/* 1965 */         parseField();
/* 1966 */       } catch (SyntaxError syntaxError) {
/* 1967 */         recoverField(classDefinition2);
/*      */       } finally {
/* 1969 */         this.curClass = classDefinition1;
/*      */       } 
/*      */     } 
/* 1972 */     expect(139);
/*      */ 
/*      */     
/* 1975 */     this.actions.endClass(this.scanner.prevPos, classDefinition2);
/* 1976 */     return classDefinition2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void recoverFile() throws IOException {
/*      */     while (true) {
/* 1986 */       switch (this.token) {
/*      */         case 111:
/*      */         case 114:
/*      */           return;
/*      */ 
/*      */         
/*      */         case 138:
/* 1993 */           match(138, 139);
/* 1994 */           scan();
/*      */           continue;
/*      */         
/*      */         case 140:
/* 1998 */           match(140, 141);
/* 1999 */           scan();
/*      */           continue;
/*      */         
/*      */         case 142:
/* 2003 */           match(142, 143);
/* 2004 */           scan();
/*      */           continue;
/*      */ 
/*      */         
/*      */         case -1:
/*      */           return;
/*      */       } 
/*      */       
/* 2012 */       scan();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseFile() {
/*      */     try {
/*      */       try {
/* 2024 */         if (this.token == 115) {
/*      */           
/* 2026 */           long l = scan();
/* 2027 */           IdentifierToken identifierToken = parseName(false);
/* 2028 */           expect(135);
/* 2029 */           this.actions.packageDeclaration(l, identifierToken);
/*      */         } 
/* 2031 */       } catch (SyntaxError syntaxError) {
/* 2032 */         recoverFile();
/*      */       } 
/* 2034 */       while (this.token == 110) {
/*      */         
/*      */         try {
/* 2037 */           long l = scan();
/* 2038 */           IdentifierToken identifierToken = parseName(true);
/* 2039 */           expect(135);
/* 2040 */           if (identifierToken.id.getName().equals(idStar)) {
/* 2041 */             identifierToken.id = identifierToken.id.getQualifier();
/* 2042 */             this.actions.importPackage(l, identifierToken); continue;
/*      */           } 
/* 2044 */           this.actions.importClass(l, identifierToken);
/*      */         }
/* 2046 */         catch (SyntaxError syntaxError) {
/* 2047 */           recoverFile();
/*      */         } 
/*      */       } 
/*      */       
/* 2051 */       while (this.token != -1) {
/*      */         try {
/* 2053 */           switch (this.token) {
/*      */             
/*      */             case 111:
/*      */             case 114:
/*      */             case 120:
/*      */             case 121:
/*      */             case 128:
/*      */             case 130:
/*      */             case 131:
/* 2062 */               parseClass();
/*      */               continue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             case 135:
/* 2072 */               scan();
/*      */               continue;
/*      */ 
/*      */             
/*      */             case -1:
/*      */               return;
/*      */           } 
/*      */ 
/*      */           
/* 2081 */           this.env.error(this.pos, "toplevel.expected");
/* 2082 */           throw new SyntaxError();
/*      */         }
/* 2084 */         catch (SyntaxError syntaxError) {
/* 2085 */           recoverFile();
/*      */         } 
/*      */       } 
/* 2088 */     } catch (IOException iOException) {
/* 2089 */       this.env.error(this.pos, "io.exception", this.env.getSource());
/*      */       return;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long scan() throws IOException {
/* 2112 */     if (this.scanner != this && this.scanner != null) {
/* 2113 */       long l = this.scanner.scan();
/* 2114 */       this.token = this.scanner.token;
/* 2115 */       this.pos = this.scanner.pos;
/* 2116 */       return l;
/*      */     } 
/* 2118 */     return super.scan();
/*      */   }
/*      */   
/*      */   public void match(int paramInt1, int paramInt2) throws IOException {
/* 2122 */     if (this.scanner != this) {
/* 2123 */       this.scanner.match(paramInt1, paramInt2);
/* 2124 */       this.token = this.scanner.token;
/* 2125 */       this.pos = this.scanner.pos;
/*      */       return;
/*      */     } 
/* 2128 */     super.match(paramInt1, paramInt2);
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\Parser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */