
public class MyParser {
	public static void main( String[] args )
	{
//		MiniJavaParser parser;
		try
		{
			MiniJavaParser parser=new MiniJavaParser( new java.io.FileInputStream(args[0]) );
			
			MiniJavaParser.Goal();
			MiniJavaParser.jjtree.rootNode().resolveForwardDeclarations( );
			System.out.println( "Successfull parsing of "+args[0] );
			MiniJavaParser.jjtree.rootNode().dump();
			
			MiniJavaParser.jjtree.rootNode().generateRelations();
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
	}

}
