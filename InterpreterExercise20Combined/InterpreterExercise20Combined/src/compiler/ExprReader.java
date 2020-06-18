package compiler;

public class ExprReader extends ExprReaderIntf {

    public ExprReader(SymbolTable symbolTable, LexerIntf lexer, CompileEnvIntf compileEnv) throws Exception {
        super(symbolTable, lexer, compileEnv);
    }

    @Override
    // atomicExpr: INTEGER
    // atomicExpr: LPAREN expr RPAREN
    // atomicExpr: IDENT
    public void getAtomicExpr() throws Exception {
        Token token = m_lexer.lookAheadToken();
        // select
        if (token.m_type == Token.Type.INTEGER) {
            m_lexer.advance();
            // create instruction
            InstrIntf numberInstr = new Instr.PushNumberInstr(token.m_intValue);
            m_compileEnv.addInstr(numberInstr);
        } else if (token.m_type == Token.Type.LPAREN) {
            m_lexer.advance();
            getExpr();
            m_lexer.expect(Token.Type.RPAREN);
        } else if (token.m_type == Token.Type.IDENT) {
            m_lexer.advance();
            // create instruction
            InstrIntf variableInstr = new Instr.VariableInstr(token.m_stringValue);
            m_compileEnv.addInstr(variableInstr);
        } else {
            throw new ParserException("Unexpected Token: ", token.toString(), m_lexer.getCurrentLocationMsg(),
                    "numerical expression");
        }
    }

    @Override
    public void getUnaryExpr() throws Exception {
        Token token = m_lexer.lookAheadToken();
        int sign = 0;
        while (token.m_type == Token.Type.MINUS) {
            m_lexer.advance();
            sign += 1;
            token = m_lexer.lookAheadToken();
        }
        getAtomicExpr();
        // create instruction
        while (sign > 0) {
            InstrIntf unaryMinusExpr = new Instr.UnaryMinusInstr();
            m_compileEnv.addInstr(unaryMinusExpr);
            sign -= 1;
        }
    }

    @Override
    public void getProduct() throws Exception {
        getUnaryExpr();
        Token token = m_lexer.lookAheadToken();
        while (token.m_type == Token.Type.MUL || token.m_type == Token.Type.DIV) {
            m_lexer.advance();
            getUnaryExpr();
            if (token.m_type == Token.Type.MUL) {
                m_compileEnv.addInstr(new Instr.MultiplyInstr());
            } else {
                m_compileEnv.addInstr(new Instr.DivisionInstr());
            }
            token = m_lexer.lookAheadToken();
        }
    }

    public void getAddExpr() throws Exception {
        getProduct();
        Token token = m_lexer.lookAheadToken();
        while (token.m_type == Token.Type.PLUS || token.m_type == Token.Type.MINUS) {
            m_lexer.advance();
            getProduct();
            if (token.m_type == Token.Type.PLUS) {
                m_compileEnv.addInstr(new Instr.AddInstr());
            } else {
                m_compileEnv.addInstr(new Instr.SubInstr());
            }
            token = m_lexer.lookAheadToken();
        }
    }

    @Override
    public void getExpr() throws Exception {
        getLogicalExpr();
    }

    public void getCompareExpr() throws Exception {
        getAddExpr();
        Token token = m_lexer.lookAheadToken();
        while (token.m_type == Token.Type.GREATER || token.m_type == Token.Type.LESS
                || token.m_type == Token.Type.EQUAL) {
            m_lexer.advance();
            getAddExpr();
            if (token.m_type == Token.Type.GREATER) {
                m_compileEnv.addInstr(new Instr.CompareGreaterInstr());
            } else if (token.m_type == Token.Type.LESS) {
                m_compileEnv.addInstr(new Instr.CompareLessInstr());
            } else if (token.m_type == Token.Type.EQUAL) {
                m_compileEnv.addInstr(new Instr.CompareEqualsInstr());
            }
            token = m_lexer.lookAheadToken();
        }
    }

    public void getLogicalExpr() throws Exception {
        getCompareExpr();
        Token token = m_lexer.lookAheadToken();
        while (token.m_type == Token.Type.AND || token.m_type == Token.Type.OR || token.m_type == Token.Type.BITWISEAND
                || token.m_type == Token.Type.BITWISEOR) {
            m_lexer.advance();
            getCompareExpr();
            if (token.m_type == Token.Type.AND) {
                m_compileEnv.addInstr(new Instr.LogicalAndInstr());
            } else if (token.m_type == Token.Type.OR) {
                m_compileEnv.addInstr(new Instr.LogicalOrInstr());
            } else if (token.m_type == Token.Type.BITWISEAND) {
                m_compileEnv.addInstr(new Instr.LogicalBitWiseAndInstr());
            } else if (token.m_type == Token.Type.BITWISEOR) {
                m_compileEnv.addInstr(new Instr.LogicalBitWiseOrInstr());
            }
            token = m_lexer.lookAheadToken();
        }
    }

}