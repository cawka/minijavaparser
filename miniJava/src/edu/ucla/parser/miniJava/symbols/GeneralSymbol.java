/**
 * 
 */
package edu.ucla.parser.miniJava.symbols;

import edu.ucla.parser.miniJava.MyNode;

public class GeneralSymbol {
		public GeneralSymbol( SymbId name, String file, String line ) { 
//			_path=path;
			_name=name; 
			_file=file; 
			_line=line; 
		}

		public SymbId _name;
		public String _file;
		public String _line;
		
		public String toString() 
		{
			return _name.toString()+" ["+_line+"]";
		}
		
		public void dump( ) { dump(""); }
		public void dump( String prefix ) {
			System.out.println( prefix+this );// ("+this.getClass()+")" );
		}
		
		public void resolveForwardDeclarations( MyNode n ) { }
	}