/**
 * 
 */
package edu.ucla.parser.miniJava.symbols;

import edu.ucla.parser.miniJava.MyNode;

public class ReturnVariable extends Variable
{
	public ReturnVariable( SymbId name, String file, String line )
	{
		super( name,"",file,line );
	}
	
	public void resolveForwardDeclarations( MyNode n ) //nothing to resolve before Calls resolving
	{
	}
}