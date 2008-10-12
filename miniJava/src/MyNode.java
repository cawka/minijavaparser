//import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MyNode {
	/**
	 * Symbol table
	 * 
	 * List of all symbols presented in the program
	 * 
	 * Each symbol should have type, and depending on type - parameters
	 * So. Let it be hierarchical class model
	 * 
	 * 
	 */
	
	protected static Map<SymbId,Symbols.Variable> _symbols=new TreeMap<SymbId,Symbols.Variable>();
	protected static Map<SymbId,Symbols.Variable> _fields =new TreeMap<SymbId,Symbols.Variable>();
	
	protected static Map<SymbId,Symbols.Class>    _types  =new TreeMap<SymbId,Symbols.Class>();
	protected static Map<SymbId,Symbols.Function> _methods=new TreeMap<SymbId,Symbols.Function>();
	protected static List<Symbols.Heap>           _heaps  =new LinkedList<Symbols.Heap>();
	
//	protected static List<Relation<Symbols.Variable,Symbols.Variable>>	_vP0=new LinkedList<Relation<Symbols.Variable,Symbols.Variable>>();
	protected static List<Relations.vP> 		  _vP0    =new LinkedList<Relations.vP>();
	protected static List<Relations.vP>			  _assign =new LinkedList<Relations.vP>();
	
	protected static List<Symbols.Call>			  _calls  =new LinkedList<Symbols.Call>();

	protected static List<Symbols.DefferredVariable> _deffered_vars=new LinkedList<Symbols.DefferredVariable>();
	
	public void dump( ) 
	{
		System.out.println( "== Classes ==" );
		for( Iterator<Symbols.Class>    i=_types.values().iterator();   i.hasNext(); ) i.next().dump();

//		System.out.println( "== Class methods ==" );
//		for( Iterator<Symbols.Function> i=_methods.values().iterator(); i.hasNext(); ) i.next().dump();

		System.out.println( "== Variables ==" );
		for( Iterator<Symbols.Variable>  i=_symbols.values().iterator(); i.hasNext(); ) i.next().dump();
		System.out.println( "== Fields ==" );
		for( Iterator<Symbols.Variable>  i=_fields.values().iterator();  i.hasNext(); ) i.next().dump();

		System.out.println( "== Heap Symbols ==" );
		for( Iterator<Symbols.Heap>      i=_heaps.iterator(); i.hasNext(); ) i.next().dump();

		System.out.println( "== Relations: vP0 ==" );
		for( Iterator<Relations.vP>      i=_vP0.iterator();   i.hasNext(); ) i.next().dump();
		System.out.println( "== Relations: assign ==" );
		for( Iterator<Relations.vP>      i=_assign.iterator();i.hasNext(); ) i.next().dump();
	}
	
	static public String pos( JavaCharStream stream )
	{
		return "@l"+JavaCharStream.getBeginLine()+",c"+JavaCharStream.getBeginColumn();
	}
	
	public void resolveForwardDeclarations( )
	{
//		_types.put( new SymbId("int"),     new Symbols.Class(new SymbId("int"),    "","java",null) );
		_types.put( new SymbId("int[]"),   new Symbols.Class(new SymbId("int[]"),  "","java",null) );
//		_types.put( new SymbId("boolean"), new Symbols.Class(new SymbId("boolean"),"","java",null) );
		
//		new SymbId("int").equals( new SymbId("int") );
//		new SymbId("int").compareTo( new SymbId("int") );
		
		for( Iterator<Symbols.Class>    i=_types.values().iterator();   i.hasNext(); ) i.next().resolveForwardDeclarations(this);
		for( Iterator<Symbols.Function> i=_methods.values().iterator(); i.hasNext(); ) i.next().resolveForwardDeclarations(this);
		for( Iterator<Symbols.Variable> i=_symbols.values().iterator(); i.hasNext(); ) i.next().resolveForwardDeclarations(this);
		for( Iterator<Symbols.Heap>     i=_heaps.iterator();            i.hasNext(); ) i.next().resolveForwardDeclarations(this);
		for( Iterator<Symbols.DefferredVariable> i=_deffered_vars.iterator(); i.hasNext(); ) i.next().resolveVariable(this);

		for( Iterator<Symbols.Call>     i=_calls.iterator();            i.hasNext(); ) i.next().resolveForwardDeclarations(this);
	}
	
	public void generateRelations( )
	{
//		System.out.println( "Test, "+this.getClass().toString() );
		throw new Error( "Unknown program behavior" );
	}
	
	public Symbols.Variable variableLookup( String name, SymbId scope )
	{
		SymbId probe_scope=scope;
		Symbols.Variable var;
		do 
		{
			var=_symbols.get( new SymbId(probe_scope,name) );
			if( var!=null ) return var;
			probe_scope=probe_scope.getPrevScope( );
		} while( probe_scope._scope.size()>0 );
		
		if( name!="this" )
		{
			Symbols.DefferredVariable var_def=new Symbols.DefferredVariable(
					new SymbId(scope,name),"","tmp_var"	);
			
			_symbols.put( var_def._name, var_def );
			_deffered_vars.add( var_def );
			
			return var_def;
		}
		
		throw new Error( "Unknown variable '"+name+"' in scope "+scope );
	}

}
