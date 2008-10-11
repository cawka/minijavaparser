//import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
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
	protected static Map<SymbId,Symbols.Class>    _types  =new TreeMap<SymbId,Symbols.Class>();
	protected static Map<SymbId,Symbols.Function> _methods=new TreeMap<SymbId,Symbols.Function>();
	protected static Map<SymbId,Symbols.Variable> _heaps  =new TreeMap<SymbId,Symbols.Variable>();
	
	public void dump( ) 
	{
		System.out.println( "== Classes ==" );
		for( Iterator<Symbols.Class>    i=_types.values().iterator();   i.hasNext(); ) i.next().dump();

//		System.out.println( "== Class methods ==" );
//		for( Iterator<Symbols.Function> i=_methods.values().iterator(); i.hasNext(); ) i.next().dump();

		System.out.println( "== Variables ==" );
		for( Iterator<Symbols.Variable>  i=_symbols.values().iterator(); i.hasNext(); ) i.next().dump();
	}
	
	static public String pos( JavaCharStream stream )
	{
		return "@l"+JavaCharStream.getBeginLine()+",c"+JavaCharStream.getBeginColumn();
	}
	
	public void resolveForwardDeclarations( )
	{
		_types.put( new SymbId("int"),     new Symbols.Class(new SymbId("int"),    "","java",null) );
		_types.put( new SymbId("int[]"),   new Symbols.Class(new SymbId("int[]"),  "","java",null) );
		_types.put( new SymbId("boolean"), new Symbols.Class(new SymbId("boolean"),"","java",null) );
		
//		new SymbId("int").equals( new SymbId("int") );
//		new SymbId("int").compareTo( new SymbId("int") );
		
		for( Iterator<Symbols.Class>    i=_types.values().iterator();   i.hasNext(); ) i.next().resolveForwardDeclarations(this);
		for( Iterator<Symbols.Function> i=_methods.values().iterator(); i.hasNext(); ) i.next().resolveForwardDeclarations(this);
		for( Iterator<Symbols.Variable> i=_symbols.values().iterator(); i.hasNext(); ) i.next().resolveForwardDeclarations(this);
	}
	
	public void generateRelations( )
	{
//		System.out.println( "Test, "+this.getClass().toString() );
		throw new Error( "Unknown program behavior" );
	}
}
