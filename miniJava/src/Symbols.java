import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;


public class Symbols {

	public static class General {
		public General( SymbId name, String file, String line ) { 
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
	
	public static class Variable extends General 
		implements PrintRelation
	{
		public Variable( SymbId name, String type, String file, String line ) { 
			super( name,file,line );
			_type_s=type;
		}
		
		public Symbols.Class _type=null;
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
	};
	
	public static class Heap extends Variable 
	{
		public Heap( SymbId name, String type, String file, String line ) { 
			super( name, type, file, line );
		}
		
		public void resolveForwardDeclarations( MyNode n ) 
		{
			super.resolveForwardDeclarations( n );
			
			if( _type_s=="int[]" ) return;
			Symbols.Variable var_this=n.variableLookup( "this", new SymbId(_type_s) );
			if( var_this==this ) return;
			
			Relations.TwoSymbols vP0=new Relations.TwoSymbols(var_this,this);// var_this,this );
			n._vP0.add( vP0 );
		}
	}
	
	public static class ReturnVariable extends Variable
	{
		public ReturnVariable( SymbId name, String file, String line )
		{
			super( name,"",file,line );
		}
		
		public void resolveForwardDeclarations( MyNode n ) //nothing to resolve before Calls resolving
		{
		}
	}
	
	public static class DefferredVariable extends ReturnVariable
	{

		public DefferredVariable( SymbId name, String file, String line ) 
		{
			super(name, file, line);
		}
		
		public void resolveVariable( MyNode n )
		{
			String name=_name.getName( );
			// one more chance to find variable in the parent declarations
			Symbols.Variable current_this=n.variableLookup( "this",_name );
			if( current_this==null ) throw new Error( "Undeclared variable "+this );

			assert( current_this._type!=null );
			
			Symbols.Variable var=current_this._type.lookupVariable( name );
			if( var==null ) throw new Error( "Undeclared variable "+this );
			
			_type=var._type;
			n._assign.add( new Relations.TwoSymbols(var,this) );
		}		
	}

	public static class Function extends General {
		public Function( SymbId name, String type, String file, String line ) {
			super( name, file, line );
			_type_s=type;
			// procedure to fill parameter list with links to _symbols
		}
		
		public void resolveType()
		{
//			throw new ClassNotFoundException( "" );
		}
		
		public Symbols.Class _type;
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
	
	public static class Class extends General
		implements PrintRelation
	{
		public Class( SymbId name, String file, String line, String parent ) {
			super( name, file, line );
			_parent_s=parent;
		}
		
		
		public void dump( String prefix ) {
			super.dump( prefix );
			if( _parent!=null ) _parent.dump( prefix+"\t" );
			
//			for( Iterator<General> i=_methods.iterator(); i.hasNext(); ) i.next().dump( prefix+"\t::" );
 		}
		
		public void resolveForwardDeclarations( MyNode n ) 
		{
			if( _parent_s==null || _parent_s=="" ) return;
			_parent=n._types.get( new SymbId(_parent_s) );
			if( _parent==null ) throw new Error( "Unknown parent class '"+_parent_s+"' used for class declaration "+_name+" ["+_line+"]" );// ("+this.getClass()+")" );
		}
		
		public Function lookupMethod( String name )
		{
			Function ret=null;
			Class probe_class=this;
			while( ret==null && probe_class!=null )
			{
				ret=probe_class._methods.get( name );
				probe_class=probe_class._parent;
			}
			if( ret==null ) throw new Error( "Unknown method call "+this._name+"::"+name );
			return ret;
		}
		
		public Variable lookupVariable( String name )
		{
			Variable ret=null;
			Class probe_class=this;
			while( ret==null && probe_class!=null )
			{
				ret=probe_class._variables.get( name );
				probe_class=probe_class._parent;
			}
			if( ret==null ) throw new Error( "Unknown class variable "+this._name+"::"+name );
			return ret;
		}
		
		public String toRelation( ) throws NoRelation
		{
			String ret=_name._line+" "+_name._line;
			if( _parent!=null ) ret+="\n"+_parent._name._line+" "+_name._line;
			return ret;
		}

		/**
		 * + function list
		 * + variable list
		 */
		public Class  _parent; //< Parent class
		public String _parent_s; //< Because parent class can be defined after usage, on the first parsing cycle we sometime do not have enough information

		public Map<String,Function> _methods  =new TreeMap<String,Function>();
		public Map<String,Variable> _variables=new TreeMap<String,Variable>();
	}
	
	public static class Call extends General 
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
						if( func._return!=null ) n._assign.add( new Relations.TwoSymbols(_retvar,func._return) );
						
						Iterator<Variable> formal=func._variables.iterator( );
						Iterator<Variable> actual=_params.iterator( );
						for( int i=0; i<_params.size(); i++ )
						{
							Variable a=actual.next();
							Variable f=formal.next();
							if( a==null ) continue; //formal parameter can't be null
							n._assign.add( new Relations.TwoSymbols(f,a) );
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
	
	class ClassNotFoundException extends Error {}
}

