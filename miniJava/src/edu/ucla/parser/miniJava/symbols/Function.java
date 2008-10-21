/**
 * 
 */
package edu.ucla.parser.miniJava.symbols;

import java.util.Vector;

import edu.ucla.parser.miniJava.MyNode;

public class Function extends GeneralSymbol {
		public Function( SymbId name, String type, String file, String line ) {
			super( name, file, line );
			_type_s=type;
			// procedure to fill parameter list with links to _symbols
		}
		
		public void resolveType()
		{
//			throw new ClassNotFoundException( "" );
		}
		
		public Class _type;
		public String _type_s;
		public Variable _return;
		public Variable _this;

		public Vector<Variable> _variables=new Vector<Variable>();
		public int _invocations=0;
		
		public void resolveForwardDeclarations( MyNode n ) 
		{
			if( _type_s==null || _type_s=="" ) return;
			_type=n._types.get( new SymbId(_type_s) );
			if( _type==null ) throw new Error( "Unknown type '"+_type_s+"' used for function return type "+_name+" ["+_line+"]" );// ("+this.getClass()+")" );
		}		

		public void dump( String prefix ) {
			super.dump( prefix );
			if( _type!=null ) _type.dump( prefix+"\t: ");
		}
	}