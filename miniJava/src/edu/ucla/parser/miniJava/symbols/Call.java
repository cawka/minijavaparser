/**
 * 
 */
package edu.ucla.parser.miniJava.symbols;

import java.util.Iterator;
import java.util.Vector;

import edu.ucla.parser.miniJava.MyNode;
import edu.ucla.parser.miniJava.MyParser;
import edu.ucla.parser.miniJava.relations.TwoSymbols;

public class Call extends GeneralSymbol 
	{
		public Call( Variable variable, String method, Vector<Variable> params, 
				    String file, String line, ReturnVariable retvar, Function invoke_method )
		{
			super( new SymbId(variable._name,method),file,line );
			_variable=variable;
			_method=method;
			_params=params;
			_retvar=retvar;
			_invoke_method=invoke_method;
		}
		
		public boolean checkMethod( Function invoke )
		{
			if( invoke._variables.size()!=_params.size() ) return false;
//			if( invoke._return._type    !=_retvar._type )  return false;
			
//			Iterator<Variable> actual=_params.iterator( );
//			Iterator<Variable> formal=invoke._variables.iterator();
//			for( ; actual.hasNext() && formal.hasNext(); ) if( actual.next()._type!=formal.next()._type ) return false;
			
			return true;
		}

		public void resolveForwardDeclarations( MyNode n ) 
		{
			try
			{
				if( MyParser._algorithm.compareTo("a3")<0 &&
				    _variable._type==null ) throw new Error( "Method call for non-class variable" );
				
				/////////////////////////////////////////////////////
				// DEEPLY INCORRECT SOLUTION - do not allow virtual calls !!!
				//Function func=_variable._type.lookupMethod( _method );
				/////////////////////////////////////////////////////
				//
				// For our analysis part we should find all methods with name '_method' and having 
				// right formal parameters. For all these methods we should add temporary variable assignments
				// 
				
				// in a3 and later algorithms everything will be done by bddbddb analysis
				if( MyParser._algorithm.compareTo("a3")<0 )
				{
					Vector<Function> list=n.getMethodsByName( _method ); // let's assume we can make call for every method (do not check type)
					if( list.size()==0 ) throw new Error( "No matching functions found for method call "+_method+" call"+" ["+_line+"]" );
	
					for( Iterator<Function> iter=list.iterator(); iter.hasNext(); )
					{
						Function func=iter.next( );
						if( !checkMethod(func) ) continue;
						_retvar._type=func._type;
						if( func._return!=null ) n._assign.add( new TwoSymbols(_retvar,func._return) );
						
						Iterator<Variable> formal=func._variables.iterator( );
						Iterator<Variable> actual=_params.iterator( );
						for( int i=0; i<_params.size(); i++ )
						{
							Variable a=actual.next();
							Variable f=formal.next();
							if( a==null ) continue; //formal parameter can't be null
							n._assign.add( new TwoSymbols(f,a) );
						}
					}
				}
			}
			catch( Error e )
			{
				throw new Error( e.getMessage()+" ["+_line+"]" );
			}
		}
		
		public Variable 		_variable;
		public String 			_method;
		public Vector<Variable> _params;
		public ReturnVariable 	_retvar;
		public Function			_invoke_method;
	}