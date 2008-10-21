/**
 * 
 */
package edu.ucla.parser.miniJava.symbols;

import edu.ucla.parser.miniJava.MyNode;
import edu.ucla.parser.miniJava.relations.NoRelation;
import edu.ucla.parser.miniJava.relations.PrintRelation;

public class Variable extends GeneralSymbol 
	implements PrintRelation
{
	public Variable( SymbId name, String type, String file, String line ) { 
		super( name,file,line );
		_type_s=type;
	}
	
	public Class _type=null;
	public String _type_s;
	
	public void resolveForwardDeclarations( MyNode n ) 
	{
		if( _type_s==null || _type_s=="" ) return;
		_type=n._types.get( new SymbId(_type_s) );
		if( _type==null ) throw new Error( "Unknown type '"+_type_s+"' used for variable "+_name+" ["+_line+"]" );// ("+this.getClass()+")" );
	}
	
	public void dump( String prefix ) {
		super.dump( prefix );
		if( _type==null ) return;
		if( _type==null )
			System.out.println( prefix+"\t"+"Unknown type" );
		else
			_type.dump( prefix+"\t: ");
	}
	
	public String toRelation( ) throws NoRelation
	{
		if( _type==null ) throw new NoRelation();
		return _name._line+" "+_type._name._line;
	}

	public String toRelationDot() throws NoRelation {
		if( _type==null ) throw new NoRelation();
		return "\""+_type._name.getName()+"\" -> \""+_name.getName()+"\"";
	}
}