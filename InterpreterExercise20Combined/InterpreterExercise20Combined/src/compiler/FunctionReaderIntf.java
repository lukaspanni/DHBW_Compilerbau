package compiler;

abstract public class FunctionReaderIntf {

    SymbolTable m_symbolTable;
    FunctionTable m_functionTable;
    LexerIntf m_lexer;
    StmtReaderIntf m_stmtReader;
    CompileEnvIntf m_compileEnv;

    public FunctionReaderIntf(LexerIntf lexer, StmtReaderIntf stmtReader, CompileEnvIntf compileEnv) throws Exception {
        m_symbolTable = compileEnv.getSymbolTable();
        m_functionTable = compileEnv.getFunctionTable();
        m_lexer = lexer;
        m_stmtReader = stmtReader;
        m_compileEnv = compileEnv;
    }

    // read factor
    abstract public void getFunction() throws Exception;

    // read expression
    abstract public void getParamListEntry() throws Exception;

    // read product
    abstract public void getParamList() throws Exception;
}
