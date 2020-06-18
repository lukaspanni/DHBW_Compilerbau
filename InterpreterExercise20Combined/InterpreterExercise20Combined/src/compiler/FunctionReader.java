package compiler;

import java.util.ArrayList;
import java.util.Iterator;

public class FunctionReader extends FunctionReaderIntf {


    public FunctionReader(LexerIntf lexer, StmtReaderIntf stmtReader, CompileEnvIntf compileEnv) throws Exception {
        super(lexer, stmtReader, compileEnv);
    }

    public void getFunction() throws Exception {
        m_lexer.advance();

        m_lexer.lookAheadToken();
        Token fctIdentifierToken = m_lexer.lookAheadToken();
        m_lexer.expect(Token.Type.IDENT);
        String fctName = fctIdentifierToken.m_stringValue;

        InstrBlock outerBlock = m_compileEnv.getCurrentBlock();
        InstrBlock fctBody = m_compileEnv.createBlock();
        m_functionTable.createFunction(fctName, fctBody);
        m_compileEnv.setCurrentBlock(fctBody);

        m_lexer.expect(Token.Type.LPAREN);
        getParamListEntry();
        m_lexer.expect(Token.Type.RPAREN);
        m_stmtReader.getBlockStmt();
        InstrIntf instrReturn = new Instr.InstrReturn();
        m_compileEnv.addInstr(instrReturn);
        m_compileEnv.setCurrentBlock(outerBlock);
    }

    public void getParamListEntry() throws Exception{
        Token param =  m_lexer.lookAheadToken();

        if(param.m_type == Token.Type.IDENT){
            getParamList();
        }
    }

    public void getParamList() throws Exception {

        boolean repeat;

        ArrayList<Symbol> parList = new ArrayList<Symbol>();
        do{
            repeat = false;
            Token param =  m_lexer.lookAheadToken();
            m_lexer.expect(Token.Type.IDENT);
            String paramName = param.m_stringValue;
            Symbol parSymbol = m_symbolTable.getOrCreateSymbol(paramName);
            parList.add(parSymbol);
            Token nextToken = m_lexer.lookAheadToken();
            if(nextToken.m_type == Token.Type.COMMA){
                m_lexer.advance();
                repeat = true;
            }

        } while (repeat);

        int parIndex = 0;
        while (parIndex < parList.size()) {
        	Symbol parSymbol = parList.get(parList.size() - parIndex - 1);
            InstrIntf parInstr = new Instr.InstrPopPar(parSymbol);
            m_compileEnv.addInstr(parInstr);
            parIndex++;
        }
    }
}

