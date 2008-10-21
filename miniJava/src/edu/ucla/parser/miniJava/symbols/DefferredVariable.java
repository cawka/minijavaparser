/**
 * 
 */
package edu.ucla.parser.miniJava.symbols;

import edu.ucla.parser.miniJava.MyNode;
import edu.ucla.parser.miniJava.relations.TwoSymbols;

public class DefferredVariable extends ReturnVariable
{

	public DefferredVariable( SymbId name, String file, String line ) 
	{
		super(name, file, line);
	}
	
	public void resolveVariable( MyNode n )
	{
		String name=_name.getName( );
		// one more chance to find variable in the parent declarations
		Variable current_this=n.variableLookup( "this",_name );
		if( current_this==null ) throw new Error( "Undeclared variable "+this );

		assert( current_this._type!=null );
		
		Variable var=current_this._type.lookupVariable( name );
		if( var==null ) throw new Error( "Undeclared variable "+this );
		
		_type=var._type;
		n._assign.add( new TwoSymbols(var,this) );
	}		
}