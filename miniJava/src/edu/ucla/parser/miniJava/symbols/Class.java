/**
 * 
 */
package edu.ucla.parser.miniJava.symbols;

import java.util.Map;
import java.util.TreeMap;

import edu.ucla.parser.miniJava.MyNode;
import edu.ucla.parser.miniJava.relations.NoRelation;
import edu.ucla.parser.miniJava.relations.PrintRelation;

public class Class extends GeneralSymbol
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
		
		public String toRelationDot() throws NoRelation {
			String ret="";
			if( _parent!=null ) ret+=_parent._name.getName()+" -> "+_name.getName();
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