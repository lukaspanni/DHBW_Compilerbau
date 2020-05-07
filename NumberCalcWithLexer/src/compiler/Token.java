package compiler;

public class Token extends TokenIntf {

	public Token(Type tokenType) {
		this.m_type = tokenType;
	}

	@Override
	public String toString() {
		String str = type2String(this.m_type);
		if (this.m_type == Type.INTEGER) {
			str += " " + this.m_intValue;
		} else if (this.m_type == Type.IDENT) {
			str += " " + this.m_stringValue;
		}
		return str;
	}

	public static String type2String(Type type) {
		return type.name();
	}

}
