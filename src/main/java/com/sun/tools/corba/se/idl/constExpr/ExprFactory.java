package com.sun.tools.corba.se.idl.constExpr;

import com.sun.tools.corba.se.idl.ConstEntry;
import java.math.BigInteger;

public interface ExprFactory {
  And and(Expression paramExpression1, Expression paramExpression2);
  
  BooleanAnd booleanAnd(Expression paramExpression1, Expression paramExpression2);
  
  BooleanNot booleanNot(Expression paramExpression);
  
  BooleanOr booleanOr(Expression paramExpression1, Expression paramExpression2);
  
  Divide divide(Expression paramExpression1, Expression paramExpression2);
  
  Equal equal(Expression paramExpression1, Expression paramExpression2);
  
  GreaterEqual greaterEqual(Expression paramExpression1, Expression paramExpression2);
  
  GreaterThan greaterThan(Expression paramExpression1, Expression paramExpression2);
  
  LessEqual lessEqual(Expression paramExpression1, Expression paramExpression2);
  
  LessThan lessThan(Expression paramExpression1, Expression paramExpression2);
  
  Minus minus(Expression paramExpression1, Expression paramExpression2);
  
  Modulo modulo(Expression paramExpression1, Expression paramExpression2);
  
  Negative negative(Expression paramExpression);
  
  Not not(Expression paramExpression);
  
  NotEqual notEqual(Expression paramExpression1, Expression paramExpression2);
  
  Or or(Expression paramExpression1, Expression paramExpression2);
  
  Plus plus(Expression paramExpression1, Expression paramExpression2);
  
  Positive positive(Expression paramExpression);
  
  ShiftLeft shiftLeft(Expression paramExpression1, Expression paramExpression2);
  
  ShiftRight shiftRight(Expression paramExpression1, Expression paramExpression2);
  
  Terminal terminal(String paramString, Character paramCharacter, boolean paramBoolean);
  
  Terminal terminal(String paramString, Boolean paramBoolean);
  
  Terminal terminal(String paramString, Double paramDouble);
  
  Terminal terminal(String paramString, BigInteger paramBigInteger);
  
  Terminal terminal(String paramString, boolean paramBoolean);
  
  Terminal terminal(ConstEntry paramConstEntry);
  
  Times times(Expression paramExpression1, Expression paramExpression2);
  
  Xor xor(Expression paramExpression1, Expression paramExpression2);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\constExpr\ExprFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */