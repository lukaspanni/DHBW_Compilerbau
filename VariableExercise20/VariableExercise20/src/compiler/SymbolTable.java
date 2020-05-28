package compiler;

import java.util.HashMap;

public class SymbolTable implements SymbolTableIntf {

	private HashMap<String, Symbol> symbolTable;

	public SymbolTable() {
		this.symbolTable = new HashMap<String, Symbol>();
	}

	@Override
	public Symbol createSymbol(String symbolName) {
		return this.createSymbol(symbolName, 0);
	}

	public Symbol createSymbol(String symbolName, int value) {
		Symbol symbol = new Symbol(symbolName, value);
		this.symbolTable.put(symbolName, symbol);
		return symbol;
	}

	@Override
	public Symbol getSymbol(String symbolName) {
		return this.symbolTable.get(symbolName);
	}
}
