import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
		
		public void dump( ) { dump(""); }
		public void dump( String prefix ) {
			System.out.println( prefix+_name+" ["+_line+"] ("+this.getClass()+")" );
		}
		
		public void resolveForwardDeclarations( MyNode n ) { }
	}
	
	public static class Variable extends General {
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
			if( _type==null ) throw new Error( "Unknown type '"+_type_s+"' used for variable "+_name+" ["+_line+"] ("+this.getClass()+")" );
		}
		
		public void dump( String prefix ) {
			super.dump( prefix );
			_type.dump( prefix+"\t: ");
		}
	};
	
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
		public Vector<Variable> _variables=new Vector<Variable>();
		
		public void resolveForwardDeclarations( MyNode n ) 
		{
			if( _type_s==null || _type_s=="" ) return;
			_type=n._types.get( new SymbId(_type_s) );
			if( _type==null ) throw new Error( "Unknown type '"+_type_s+"' used for function return type "+_name+" ["+_line+"] ("+this.getClass()+")" );
		}		

		public void dump( String prefix ) {
			super.dump( prefix );
			_type.dump( prefix+"\t: ");
		}
	}
	
	public static class Class extends General {
		public Class( SymbId name, String file, String line, String parent ) {
			super( name, file, line );
			_parent_s=parent;
		}
		
		public General _parent; //< Parent class
		public String _parent_s; //< Because parent class can be defined after usage, on the first parsing cycle we sometime do not have enough information
		
		public void dump( String prefix ) {
			super.dump( prefix );
			if( _parent!=null ) _parent.dump( prefix+"\t" );
			
			for( Iterator<General> i=_methods.iterator(); i.hasNext(); ) i.next().dump( "\t::" );
 		}
		
		public void resolveForwardDeclarations( MyNode n ) 
		{
			if( _parent_s==null || _parent_s=="" ) return;
			_parent=n._types.get( new SymbId(_parent_s) );
			if( _parent==null ) throw new Error( "Unknown parent class '"+_parent_s+"' used for class declaration "+_name+" ["+_line+"] ("+this.getClass()+")" );
		}

		/**
		 * + function list
		 * + variable list
		 */
		public Vector<General> _methods=new Vector<General>();
		public Vector<General> _variables=new Vector<General>();
	}
	
	class ClassNotFoundException extends Error {}
}

