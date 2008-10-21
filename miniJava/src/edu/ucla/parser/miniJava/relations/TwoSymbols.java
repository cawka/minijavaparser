/**
 * 
 */
package edu.ucla.parser.miniJava.relations;

import edu.ucla.parser.miniJava.symbols.GeneralSymbol;

public class TwoSymbols extends UnknownRelation 
{
	public GeneralSymbol _s1;
	public GeneralSymbol _s2;
	
	public TwoSymbols( GeneralSymbol s1, GeneralSymbol s2 )
	{
		_s1=s1;
		_s2=s2;
	}
	
	public String toString()
	{
		return _s1._name._line+" "+_s2._name._line;
	}
	
	public void dump( )
	{
		System.out.println( _s1._name+" = "+_s2._name );
	}
}