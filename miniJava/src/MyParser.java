import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


public class MyParser {
	public static void main( String[] args )
	{
		if( args.length!=2 )
		{
			System.out.println(
"Program usage: \n" +
"    java MyParser <DIR> <SOURCE>\n\n" +
"<DIR>    - destination directory for .map and .bdd file placement\n" +
"<SOURCE> - source miniJava program to analyze"
);
			return;
		}
		
//		MiniJavaParser parser;
		try
		{
			MiniJavaParser parser=new MiniJavaParser( new java.io.FileInputStream(args[0]) );
			
			MiniJavaParser.Goal();
			MiniJavaParser.jjtree.rootNode().resolveForwardDeclarations( );
//			System.out.println( "Successfull parsing of "+args[0] );
			MiniJavaParser.jjtree.rootNode().dump();
			
			export( "testdir", MiniJavaParser.jjtree.rootNode() );
			
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
		File vP0=new File( dir+"/vP0.bdd" );
		exportRelationsToFile( node._vP0, vP0 );
		
		File assign=new File( dir+"/assign.bdd" );
		exportRelationsToFile( node._assign, assign );
	}
	
	public static void exportMapToFile( Collection list, File file ) throws FileNotFoundException
	{
		PrintStream V=new PrintStream( new FileOutputStream( file ) );
		
		int lines=0;
		for( Iterator<Symbols.General> i=list.iterator(); i.hasNext(); )
		{
			Symbols.General symb=i.next();
			symb._name._line=lines++;
			V.println( symb.toString() );
		}
	}

	public static void exportRelationsToFile( Collection list, File file ) throws FileNotFoundException
	{
		PrintStream V=new PrintStream( new FileOutputStream( file ) );
		
		int lines=0;
		for( Iterator<Object> i=list.iterator(); i.hasNext(); )
		{
			Object relation=i.next( );
			V.println( relation.toString() );
		}
	}
}


