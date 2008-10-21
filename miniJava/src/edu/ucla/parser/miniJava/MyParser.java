package edu.ucla.parser.miniJava;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.ucla.parser.miniJava.javacc.MiniJavaParser;
import edu.ucla.parser.miniJava.javacc.Node;
import edu.ucla.parser.miniJava.callgraph.CallGraph;
import edu.ucla.parser.miniJava.callgraph.GraphNode;
import edu.ucla.parser.miniJava.relations.PrintRelation;
import edu.ucla.parser.miniJava.symbols.Call;
import edu.ucla.parser.miniJava.symbols.Class;
import edu.ucla.parser.miniJava.symbols.Function;
import edu.ucla.parser.miniJava.symbols.GeneralSymbol;
import edu.ucla.parser.miniJava.symbols.Variable;


public class MyParser 
{
	public static String _algorithm;
	
	public static void main( String[] args )
	{
		if( args.length!=3 )
		{
			System.out.println(
"Program usage: \n" +
"    java -jar parser.jar <DIR> <SOURCE> <algrorithm>\n\n" +
"<DIR>    - destination directory for .map and .bdd file placement\n" +
"<SOURCE> - source miniJava program to analyze\n"+
"<alogrirm> - algorithm number/name"
);
			return;
		}
		
		String working_directory=args[0];
		String source_file      =args[1];
		_algorithm				=args[2];
		
		try
		{
			MiniJavaParser parser=new MiniJavaParser( new java.io.FileInputStream(source_file) );
			
			MiniJavaParser.Goal();
			
			parser.getRoot().resolveForwardDeclarations( );
			
			export( working_directory, parser.getRoot() );
			parser.getRoot().dump();
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
	}

	
	public static void export( String dir, Node node_ ) throws Exception
	{
		MyNode node=(MyNode)node_;
		//if( !new File(dir).mkdirs() ) throw new Error( "Can't create directory '"+dir+"'" );
		new File(dir).mkdirs(); //creates subdirs if necessary
		
		// export MAPS
		File V=new File( dir+"/V.map" );
		exportMapToFile( node._symbols.values(), V );
		
		File H=new File( dir+"/H.map" );
		exportMapToFile( node._heaps, H );
		
		File F=new File( dir+"/F.map" );
		exportMapToFile( node._fields.values(), F );
		
//		if( _algorithm.equals("a2") )
//		{
			File T=new File( dir+"/T.map" );
			exportMapToFile( node._types.values(), T );
//		}
		
		// export RELATIONS
		File vP0=new File( dir+"/vP0.tuples" );
		exportRelationsToFile( node._vP0, vP0, "V0:65535 H0:65535" );
		
		File assign=new File( dir+"/assign.tuples" );
		exportRelationsToFile( node._assign, assign, "V0:65535 V1:65535" );
		
		// to maintain compatibility, create blank files for load and store relations
		Vector<Object> blank=new Vector<Object>();

//		File load =new File( dir+"/load.tuples" ); 
//		exportRelationsToFile( blank, load, "V0:65535 F0:65535 V1:65535" );
//		
//		File store=new File( dir+"/store.tuples" ); 
//		exportRelationsToFile( blank, store, "V0:65535 F0:65535 V1:65535" );
		
//		if( _algorithm.equals("a2") )
//		{
			File vT=new File( dir+"/vT.tuples" );
			exportRelationsToFile( node._symbols.values(), vT, "V0:65535 T0:65535" );

			File hT=new File( dir+"/hT.tuples" );
			exportRelationsToFile( node._heaps, hT, "H0:65535 T0:65535" );
			
			File aT=new File( dir+"/aT0.tuples" );
			exportRelationsToFile( node._types.values(), aT, "T0:65535 T1:65535" );
//		}
		
		File I=new File( dir+"/I.map" );
		exportMapToFile( node._calls, I );
		
		File N=new File( dir+"/N.map" );
		exportMapToFile( node._method_names.values(), N );
		
		File M=new File( dir+"/M.map" );
		exportMapToFile( node._methods.values(), M );
		
		File cha=new File( dir+"/cha.tuples" );
		exportCha( node,cha,"T0:65535 M0:65535 N0:65535" );
		
		File actual=new File( dir+"/actual.tuples" );
		exportActual( node,actual, "I0:65535 Z0:65535 V0:65535" );
		
		File formal=new File( dir+"/formal.tuples" );
		exportFormal( node,formal, "M0:65535 Z0:65535 V0:65535" );
		
		File mI=new File( dir+"/mI.tuples" );
		exportRelationsToFile( node._mI, mI, "M0:65535 I0:65535 N0:65535" );
		
		File _return=new File( dir+"/return.tuples" );
		exportRelationsToFile( node._return, _return, "M0:65535 V0:65535" );
		
		File callreturn=new File( dir+"/callreturn.tuples" );
		exportRelationsToFile( node._callreturn, callreturn, "I0:65535 V0:65535" );

		File call_graph_orig=new File( dir+"/call_graph_orig.dot" );
		node._call_graph.dumpGraphCycles( new PrintStream(new FileOutputStream(call_graph_orig)) );

		node._call_graph.findCycles( ); node._call_graph.collapse( );
		
		File mIc=new File( dir+"/mIc.tuples" );
		exportContexts( node._call_graph, mIc, "M0:65535 C0:65535 I0:65535 C1:65535 N0:65535" );

		File call_graph=new File( dir+"/call_graph.dot" );
		node._call_graph.dumpGraph( new PrintStream(new FileOutputStream(call_graph)) );
		
		File class_diagram=new File( dir+"/class_diagram.dot" );
		exportDotGraph( node._types.values(), class_diagram );
	}
	
	public static void exportMapToFile( Collection list, File file ) throws FileNotFoundException
	{
		PrintStream f=new PrintStream( new FileOutputStream( file ) );
		
		int lines=0;
		for( Iterator<GeneralSymbol> i=list.iterator(); i.hasNext(); )
		{
			GeneralSymbol symb=i.next();
			symb._name._line=lines++;
			f.println( symb.toString() );
		}
	}

	public static void exportRelationsToFile( Collection list, File file, String header ) throws FileNotFoundException
	{
		PrintStream f=new PrintStream( new FileOutputStream( file ) );
		f.println( "# "+header );
		
		for( Iterator<PrintRelation> i=list.iterator(); i.hasNext(); )
		{
			PrintRelation relation=i.next( );
			try { f.println( relation.toRelation() ); } catch( Exception e ) { } //print relations only if relation exists
		}
	}
	
	public static void exportCha( MyNode node, File file, String header ) throws FileNotFoundException
	{
		PrintStream f=new PrintStream( new FileOutputStream( file ) );
		f.println( "# "+header );

		for( Iterator<Class> t_=node._types.values().iterator(); t_.hasNext(); )
		{
			Class t=t_.next( );
			for( Iterator<Function> m_=t._methods.values().iterator(); m_.hasNext(); )
			{
				Function m=m_.next( );
				f.println( t._name._line+" "+m._name._line+" "+node._method_names.get(m._name.getName( ))._name._line );
			}
		}
	}
	
	public static void exportActual( MyNode node, File file, String header ) throws FileNotFoundException
	{
		PrintStream f=new PrintStream( new FileOutputStream( file ) );
		f.println( "# "+header );

		for( Iterator<Call> i_=node._calls.iterator(); i_.hasNext(); )
		{
			Call i=i_.next( );
			f.println( i._name._line+" 0 "+i._variable._name._line );
			int pos=1;
			for( Iterator<Variable> v=i._params.iterator(); v.hasNext(); )
			{
				Variable var=v.next();
				if( var!=null ) f.println( i._name._line+" "+(pos)+" "+var._name._line );
				pos++;
			}
		}	
	}

	public static void exportFormal( MyNode node, File file, String header ) throws FileNotFoundException
	{
		PrintStream f=new PrintStream( new FileOutputStream( file ) );
		f.println( "# "+header );

		for( Iterator<Function> m_=node._methods.values().iterator(); m_.hasNext(); )
		{
			Function m=m_.next( );
//			if( m._this!=null ) f.println( m._name._line+" 0 "+m._this._name._line );
			int pos=1;
			for( Iterator<Variable> v=m._variables.iterator(); v.hasNext(); )
			{
				f.println( m._name._line+" "+(pos++)+" "+v.next()._name._line );
			}
		}
	}
	
	public static void exportContexts( CallGraph cg, File file, String header ) throws FileNotFoundException
	{
		PrintStream f=new PrintStream( new FileOutputStream( file ) );
		f.println( "# "+header );
//		f=System.out;

		// Step 1.
		for( Iterator<GraphNode> i=cg._all_nodes.values().iterator(); i.hasNext(); )
		{
			GraphNode n=i.next();
			if( n._in.size()==0 ) continue; //already set context number=1 and context count=1
			n._context_num=n._in.size( );
		}
		
		GraphNode node=cg._all_nodes.get( "STATIC" );
		
		// Step 2. 
		// walk through all static invocations and recursively calculate context nubmers
		for( Iterator<GraphNode> i=node._out.iterator(); i.hasNext(); )
		{
			i.next( ).calculateContexts( );
		}
		
		// Step 3. Print out context invocation edges
		int counter=0;
		for( Iterator<GraphNode> pp=cg._all_nodes_list.iterator(); pp.hasNext(); )
		{
			f.println( "# NODE" );
			GraphNode p=pp.next( );
			if( p._calls.isEmpty() ) continue;
			
			//print all single contexts for cycles
			for( Iterator<String> n=p._name.values().iterator(); n.hasNext(); )
			{
				String name=n.next( );
				List<Call> list=p._calls.get( name );
				if( list==null ) continue;
//				f.println( "Single context for loops" );
				for( Iterator<Call> i=p._calls.get( name ).iterator(); i.hasNext(); )
				{
					Call call=i.next();
//					f.printf( "# %d %s %d %s\n", 1, call, 1, name );
//					f.printf( "%d %d %d %d\n", 1, call._name._line, 1, MyNode._method_names.get( name )._name._line );

					f.printf( "# %d (%s)%s %d %s\n", 1, call._invoke_method._name.getName(), call, 1, call._name.getName() );
					f.printf( "%d %d %d %d %d\n", call._invoke_method._name._line, 
								1, call._name._line, 
								1, MyNode._method_names.get( call._name.getName() )._name._line );
				}
			}
			
			for( Iterator<String> caller_i=p._calls._calls.keySet().iterator(); caller_i.hasNext(); )
			{
				String caller=caller_i.next( );
				int j=0;
				for( Iterator<Call> call_i=p._calls.get(caller).iterator(); call_i.hasNext(); )
				{
					Call call=call_i.next( );
					String invoke=call._invoke_method._name.getName();
					if( p._name.get(invoke)!=null ) continue;
					
					for( int kk=0; kk<cg._all_nodes.get(invoke)._context_num; kk++ )
					{
						f.printf( "# %d (%s)%s %d %s\n", j+1, call._invoke_method._name.getName(), call, j+counter+1, call._name.getName() );
						f.printf( "%d %d %d %d %d\n", call._invoke_method._name._line, 
									j+1,         call._name._line, 
									j+1+counter, MyNode._method_names.get( call._name.getName() )._name._line );
						j++;
					}
				}
				counter+=j;
			}
		}
	}
	
	public static void exportDotGraph( Collection list, File file ) throws FileNotFoundException
	{
		PrintStream f=new PrintStream( new FileOutputStream( file ) );
//		f=System.out;
		f.println( "digraph classes {" );
		
		for( Iterator<PrintRelation> i=list.iterator(); i.hasNext(); )
		{
			PrintRelation relation=i.next( );
			try { f.println( relation.toRelationDot() ); } catch( Exception e ) { } //print relations only if relation exists
		}
		f.println( "}" );
	}

}

