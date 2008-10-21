package edu.ucla.parser.miniJava;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import edu.ucla.parser.miniJava.callgraph.CallGraph;
import edu.ucla.parser.miniJava.callgraph.GraphNode;
import edu.ucla.parser.miniJava.javacc.JavaCharStream;
import edu.ucla.parser.miniJava.relations.TwoSymbols;
import edu.ucla.parser.miniJava.relations.mI;
import edu.ucla.parser.miniJava.symbols.Call;
import edu.ucla.parser.miniJava.symbols.Class;
import edu.ucla.parser.miniJava.symbols.DefferredVariable;
import edu.ucla.parser.miniJava.symbols.Function;
import edu.ucla.parser.miniJava.symbols.GeneralSymbol;
import edu.ucla.parser.miniJava.symbols.Heap;
import edu.ucla.parser.miniJava.symbols.SymbId;
import edu.ucla.parser.miniJava.symbols.Variable;

public class MyNode 
{
	public SymbId _name;
	
	public static Map<SymbId,Variable> _symbols=new TreeMap<SymbId,Variable>();
	public static Map<SymbId,Variable> _fields =new TreeMap<SymbId,Variable>();
	
	public static Map<SymbId,Class>    _types  =new TreeMap<SymbId,Class>();
	public static Map<SymbId,Function> _methods=new TreeMap<SymbId,Function>();
	public static Map<String,GeneralSymbol>  _method_names=new TreeMap<String,GeneralSymbol>();
	
	public static List<Heap>           _heaps  =new LinkedList<Heap>();
	
//	public static List<Relation<Symbols.Variable,Symbols.Variable>>	_vP0=new LinkedList<Relation<Symbols.Variable,Symbols.Variable>>();
	public static List<TwoSymbols>	  _vP0    =new LinkedList<TwoSymbols>();
	public static List<TwoSymbols>	  _assign =new LinkedList<TwoSymbols>();
	
	public static List<Call>			  _calls  =new LinkedList<Call>();

	public static List<DefferredVariable> _deffered_vars=new LinkedList<DefferredVariable>();
	
	public static List<mI>			 _mI      =new LinkedList<mI>();
	public static List<TwoSymbols>  _callreturn=new LinkedList<TwoSymbols>();
	public static List<TwoSymbols>  _return  =new LinkedList<TwoSymbols>();
	
	public static CallGraph					_call_graph=new CallGraph();
		
	public MyNode( )
	{
		if( _methods.get(new SymbId("STATIC"))==null ) 
			_methods.put( new SymbId("STATIC"), new Function(new SymbId("STATIC"),"","","analysis") );
		
		if( _call_graph._all_nodes.get("STATIC")==null )
			_call_graph._all_nodes.put( "STATIC", new GraphNode("STATIC") );
	}

	public void dump( ) 
	{
		System.out.println( "== Classes ==" );
		for( Iterator<Class>    i=_types.values().iterator();   i.hasNext(); ) i.next().dump();

//		System.out.println( "== Class methods ==" );
//		for( Iterator<Symbols.Function> i=_methods.values().iterator(); i.hasNext(); ) i.next().dump();

		System.out.println( "== Variables ==" );
		for( Iterator<Variable>  i=_symbols.values().iterator(); i.hasNext(); ) i.next().dump();
		System.out.println( "== Fields ==" );
		for( Iterator<Variable>  i=_fields.values().iterator();  i.hasNext(); ) i.next().dump();
		System.out.println( "== Methods ==" );
		for( Iterator<Function>  i=_methods.values().iterator();  i.hasNext(); ) i.next().dump();
		System.out.println( "== Calls ==" );
		for( Iterator<Call>      i=_calls.iterator();  i.hasNext(); ) i.next().dump();

		System.out.println( "== Heap Symbols ==" );
		for( Iterator<Heap>      i=_heaps.iterator(); i.hasNext(); ) i.next().dump();

		System.out.println( "== Relations: vP0 ==" );
		for( Iterator<TwoSymbols>      i=_vP0.iterator();   i.hasNext(); ) i.next().dump();
		System.out.println( "== Relations: assign ==" );
		for( Iterator<TwoSymbols>      i=_assign.iterator();i.hasNext(); ) i.next().dump();

		System.out.println( "== Relations: mI (method / call / name) ==" );
		for( Iterator<mI>      i=_mI.iterator();i.hasNext(); ) i.next().dump();
	
		System.out.println( "== Relations: return ==" );
		for( Iterator<TwoSymbols>      i=_return.iterator();i.hasNext(); ) i.next().dump();
		System.out.println( "== Relations: callreturn ==" );
		for( Iterator<TwoSymbols>      i=_callreturn.iterator();i.hasNext(); ) i.next().dump();
	}
	
	static public String pos( JavaCharStream stream )
	{
		return "@l"+JavaCharStream.getBeginLine()+",c"+JavaCharStream.getBeginColumn();
	}
	
	public void resolveForwardDeclarations( )
	{
//		_types.put( new SymbId("int"),     new Symbols.Class(new SymbId("int"),    "","java",null) );
		_types.put( new SymbId("int[]"),   new Class(new SymbId("int[]"),  "","java",null) );
//		_types.put( new SymbId("boolean"), new Symbols.Class(new SymbId("boolean"),"","java",null) );
		
//		new SymbId("int").equals( new SymbId("int") );
//		new SymbId("int").compareTo( new SymbId("int") );
		
		for( Iterator<Class>    i=_types.values().iterator();   i.hasNext(); ) i.next().resolveForwardDeclarations(this);
		for( Iterator<Function> i=_methods.values().iterator(); i.hasNext(); ) i.next().resolveForwardDeclarations(this);
		for( Iterator<Variable> i=_symbols.values().iterator(); i.hasNext(); ) i.next().resolveForwardDeclarations(this);
		for( Iterator<Heap>     i=_heaps.iterator();            i.hasNext(); ) i.next().resolveForwardDeclarations(this);
		for( Iterator<DefferredVariable> i=_deffered_vars.iterator(); i.hasNext(); ) i.next().resolveVariable(this);

		for( Iterator<Call>     i=_calls.iterator();            i.hasNext(); ) i.next().resolveForwardDeclarations(this);

		for( Iterator<Function> i=_methods.values().iterator(); i.hasNext(); ) 
		{
			String name=i.next()._name.getName( );
			if( _method_names.get(name)==null )
				_method_names.put( name, new GeneralSymbol(new SymbId(name),"","") );
		}
	}
	
	public void generateRelations( )
	{
//		System.out.println( "Test, "+this.getClass().toString() );
		throw new Error( "Unknown program behavior" );
	}
	
	public Vector<Function> getMethodsByName( String name )
	{
		Vector<Function> ret=new Vector<Function>( );
		for( Iterator<Function> i=this._methods.values().iterator(); i.hasNext(); )
		{
			Function func=i.next( );
			if( func._name.getName().equals(name) ) ret.add( func );
		}
		return ret;
	}
	
	public Variable variableLookup( String name, SymbId scope )
	{
		SymbId probe_scope=scope;
		Variable var;
		do 
		{
			var=_symbols.get( new SymbId(probe_scope,name) );
			if( var!=null ) return var;
			probe_scope=probe_scope.getPrevScope( );
		} while( probe_scope._scope.size()>0 );
		
		if( name!="this" )
		{
			DefferredVariable var_def=new DefferredVariable(
					new SymbId(scope,name),"","tmp_var"	);
			
			_symbols.put( var_def._name, var_def );
			_deffered_vars.add( var_def );
			
			return var_def;
		}
		
		throw new Error( "Unknown variable '"+name+"' in scope "+scope );
	}

}
