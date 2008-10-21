/**
 * 
 */
package edu.ucla.parser.miniJava.symbols;

import edu.ucla.parser.miniJava.MyNode;
import edu.ucla.parser.miniJava.relations.TwoSymbols;

public class Heap extends Variable 
{
	public Heap( SymbId name, String type, String file, String line ) { 
		super( name, type, file, line );
	}
	
	public void resolveForwardDeclarations( MyNode n ) 
	{
		super.resolveForwardDeclarations( n );
		
		if( _type_s=="int[]" ) return;
		Variable var_this=n.variableLookup( "this", new SymbId(_type_s) );
		if( var_this==this ) return;
		
		TwoSymbols vP0=new TwoSymbols(var_this,this);// var_this,this );
		n._vP0.add( vP0 );
	}
}