package lexer_jlex;


class TinyLexer {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 65536;
	private final int YY_EOF = 65537;

  private LexicalItemFactory itemfact;
  public String getLexeme() {
    return yytext();
  }
  public int getRow() {
    return yyline+1;
  }
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	TinyLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	TinyLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private TinyLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

  itemfact = new LexicalItemFactory(this);
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NOT_ACCEPT,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NOT_ACCEPT,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NOT_ACCEPT,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,65538,
"33:8,1:3,33:2,1,33:18,1,29,33:4,2,33,31,30,24,22,3,23,5,25,4:10,33,32,28,26" +
",27,33:2,20:4,6,20:21,33:4,21,33,17,8,20,19,7,16,20:5,10,13,11,9,20:2,15,18" +
",14,12,20:5,33:65413,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,52,
"0,1:2,2,3,4,5:2,1:2,6,7,8,1:4,4,1:4,4:6,9,5,10,11,12,1,13,14,15,16,17,18,19" +
",20,21,13,22,23,24,25,26,27,28,29")[0];

	private int yy_nxt[][] = unpackFromString(30,34,
"1,2,3,29,4,33,5:2,48,31,5,42,5:2,49,5,51,44,5:3,33,6,7,8,9,10,11,12,36,13,1" +
"4,15,33,-1:36,16,-1:35,4,28,32:2,-1:30,5,-1,5:16,-1:16,4,-1:55,18,-1:33,19," +
"-1:33,20,-1:11,30,-1:33,30,-1,32:2,-1:30,5,-1,5:9,17,5:6,-1:15,43,34,-1:17," +
"43:2,-1:14,34,-1:33,5,-1,5:8,22,5:7,-1:38,21,-1:11,5,-1,5:7,23,5:8,-1:16,5," +
"-1,5:13,24,5:2,-1:16,5,-1,5:4,25,5:11,-1:16,5,-1,5,26,5:14,-1:16,5,-1,5,27," +
"5:14,-1:16,5,-1,5:3,35,5:2,37,5:9,-1:16,5,-1,5:5,38,5:10,-1:16,5,-1,5:3,39," +
"5:12,-1:16,5,-1,5:6,40,5:9,-1:16,5,-1,5:12,41,5:3,-1:16,5,-1,5:3,45,5:12,-1" +
":16,5,-1,5:9,46,5:6,-1:16,5,-1,5:4,47,5:11,-1:16,5,-1,5:11,50,5:4,-1:12");

	public LexicalItem yylex ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

  return itemfact.getItem("EOF");
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{}
					case -3:
						break;
					case 3:
						{itemfact.error();}
					case -4:
						break;
					case 4:
						{return itemfact.getItem("LREAL");}
					case -5:
						break;
					case 5:
						{return itemfact.getItem("ID");}
					case -6:
						break;
					case 6:
						{return itemfact.getItem("PLUS");}
					case -7:
						break;
					case 7:
						{return itemfact.getItem("MINUS");}
					case -8:
						break;
					case 8:
						{return itemfact.getItem("MUL");}
					case -9:
						break;
					case 9:
						{return itemfact.getItem("DIV");}
					case -10:
						break;
					case 10:
						{return itemfact.getItem("IS");}
					case -11:
						break;
					case 11:
						{return itemfact.getItem("GT");}
					case -12:
						break;
					case 12:
						{return itemfact.getItem("LT");}
					case -13:
						break;
					case 13:
						{return itemfact.getItem("PCL");}
					case -14:
						break;
					case 14:
						{return itemfact.getItem("POP");}
					case -15:
						break;
					case 15:
						{return itemfact.getItem("EOL");}
					case -16:
						break;
					case 16:
						{return itemfact.getItem("SPROG");}
					case -17:
						break;
					case 17:
						{return itemfact.getItem("OR");}
					case -18:
						break;
					case 18:
						{return itemfact.getItem("EQ");}
					case -19:
						break;
					case 19:
						{return itemfact.getItem("GEQ");}
					case -20:
						break;
					case 20:
						{return itemfact.getItem("LEQ");}
					case -21:
						break;
					case 21:
						{return itemfact.getItem("NEQ");}
					case -22:
						break;
					case 22:
						{return itemfact.getItem("NOT");}
					case -23:
						break;
					case 23:
						{return itemfact.getItem("NUM");}
					case -24:
						break;
					case 24:
						{return itemfact.getItem("AND");}
					case -25:
						break;
					case 25:
						{return itemfact.getItem("BOOL");}
					case -26:
						break;
					case 26:
						{return itemfact.getItem("TRUE");}
					case -27:
						break;
					case 27:
						{return itemfact.getItem("FALSE");}
					case -28:
						break;
					case 29:
						{itemfact.error();}
					case -29:
						break;
					case 30:
						{return itemfact.getItem("LREAL");}
					case -30:
						break;
					case 31:
						{return itemfact.getItem("ID");}
					case -31:
						break;
					case 33:
						{itemfact.error();}
					case -32:
						break;
					case 34:
						{return itemfact.getItem("LREAL");}
					case -33:
						break;
					case 35:
						{return itemfact.getItem("ID");}
					case -34:
						break;
					case 36:
						{itemfact.error();}
					case -35:
						break;
					case 37:
						{return itemfact.getItem("ID");}
					case -36:
						break;
					case 38:
						{return itemfact.getItem("ID");}
					case -37:
						break;
					case 39:
						{return itemfact.getItem("ID");}
					case -38:
						break;
					case 40:
						{return itemfact.getItem("ID");}
					case -39:
						break;
					case 41:
						{return itemfact.getItem("ID");}
					case -40:
						break;
					case 42:
						{return itemfact.getItem("ID");}
					case -41:
						break;
					case 44:
						{return itemfact.getItem("ID");}
					case -42:
						break;
					case 45:
						{return itemfact.getItem("ID");}
					case -43:
						break;
					case 46:
						{return itemfact.getItem("ID");}
					case -44:
						break;
					case 47:
						{return itemfact.getItem("ID");}
					case -45:
						break;
					case 48:
						{return itemfact.getItem("ID");}
					case -46:
						break;
					case 49:
						{return itemfact.getItem("ID");}
					case -47:
						break;
					case 50:
						{return itemfact.getItem("ID");}
					case -48:
						break;
					case 51:
						{return itemfact.getItem("ID");}
					case -49:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
