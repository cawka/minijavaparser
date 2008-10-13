import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;


public class MyParser {
	public static void main( String[] args )
	{
		if( args.length!=2 )
		{
			System.out.println(
"Program usage: \n" +
"    java -jar parser.jar <DIR> <SOURCE>\n\n" +
"<DIR>    - destination directory for .map and .bdd file placement\n" +
"<SOURCE> - source miniJava program to analyze"
);
			return;
		}
		
		String working_directory=args[0];
		String source_file      =args[1];
		
//		MiniJavaParser parser;
		try
		{
			MiniJavaParser parser=new MiniJavaParser( new java.io.FileInputStream(source_file) );
			
			MiniJavaParser.Goal();
			MiniJavaParser.jjtree.rootNode().resolveForwardDeclarations( );
//			System.out.println( "Successfull parsing of "+args[0] );
			
			export( working_directory, MiniJavaParser.jjtree.rootNode() );
			MiniJavaParser.jjtree.rootNode().dump();
			
//			MiniJavaParser.jjtree.rootNode().generateRelations();
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
		
		// export RELATIONS
		File vP0=new File( dir+"/vP0.tuples" );
		exportRelationsToFile( node._vP0, vP0, "V0:65535 H0:65535" );
		
		File assign=new File( dir+"/assign.tuples" );
		exportRelationsToFile( node._assign, assign, "V0:65535 V1:65535" );
		
		// to maintain compatibility, create blank files for load and store relations
		Vector<Object> blank=new Vector<Object>();

		File load =new File( dir+"/load.bdd" ); 
		exportRelationsToFile( blank, load, "V0:65535 F0:65535 V1:65535" );
		
		File store=new File( dir+"/store.bdd" ); 
		exportRelationsToFile( blank, store, "V0:65535 F0:65535 V1:65535" );
	}
	
	public static void exportMapToFile( Collection list, File file ) throws FileNotFoundException
	{
		PrintStream f=new PrintStream( new FileOutputStream( file ) );
		
		int lines=1;
		for( Iterator<Symbols.General> i=list.iterator(); i.hasNext(); )
		{
			Symbols.General symb=i.next();
			symb._name._line=lines++;
			f.println( symb.toString() );
		}
	}

	public static void exportRelationsToFile( Collection list, File file, String header ) throws FileNotFoundException
	{
		PrintStream f=new PrintStream( new FileOutputStream( file ) );
		f.println( "# "+header );
		
		for( Iterator<Object> i=list.iterator(); i.hasNext(); )
		{
			Object relation=i.next( );
			f.println( relation.toString() );
		}
	}
}


