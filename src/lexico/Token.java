package lexico;

public class Token {
    public static final int TK_IDENTIFIER  = 0;
	public static final int TK_NUMBER      = 1;
	public static final int TK_OPERATORA    = 2;
	public static final int TK_PUNCTUATION = 3;
	public static final int TK_ASSIGN      = 4;
        public static final int TK_OPERATORR     = 5;
        public static final int TK_CONDITIONAL = 6;
        public static final int TK_CONDITIONAL2 = 7;
        public static final int TK_CONDITIONAL3 = 8;
        public static final int TK_LOOP = 9;
        public static final int TK_LOOP2 = 10;
	
	public static final String TK_TEXT[] = {
			"IDENTIFICADOR", "NUMERO", "OPERADOR ARITMÉTICO", "PONTUAÇÃO", "ATRIBUIÇÃO","OPERADOR RELACIONAL","OPERADOR CONDICIONAL IF","OPERADOR CONDICIONAL THEN","OPERADOR CONDICIONAL ELSE","OPERADOR DE REPETIÇÃO WHILE","OPERADOR DE REPETIÇÃO DO"
	};
        
        
	
	private int    type;
	private String text;
	private int    line;
	private int    column;
	
	public Token(int type, String text) {
		super();
		this.type = type;
		this.text = text;
	}

	public Token() {
		super();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "tipo=" + TK_TEXT[type] + ", texto=" +"'"+ text +"'"+ ", linha = "+line;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
}
