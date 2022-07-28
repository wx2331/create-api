package com.sun.codemodel.internal;

public interface JAssignmentTarget extends JGenerable, JExpression {
  JExpression assign(JExpression paramJExpression);
  
  JExpression assignPlus(JExpression paramJExpression);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JAssignmentTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */