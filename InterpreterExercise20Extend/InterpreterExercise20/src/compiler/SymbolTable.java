package compiler;

import java.util.HashMap;

public class SymbolTable implements SymbolTableIntf {

	private HashMap<String, Symbol> m_symbolMap;
	
	public SymbolTable() {
		m_symbolMap = new HashMap<String, Symbol>();
	}

	public Symbol getOrCreateSymbol(String symbolName) {
		Symbol symbol = m_symbolMap.get(symbolName);
		if (symbol != null) {
			return symbol;
		} else {
			symbol = new Symbol(symbolName, 0);
			m_symbolMap.put(symbolName, symbol);
			return symbol;
		}
	}

	public Symbol getSymbol(String symbolName) {
		Symbol symbol = m_symbolMap.get(symbolName);
		return symbol;
	}
}
