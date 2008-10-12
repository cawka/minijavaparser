class TestClasses {
	public static void main( String[] preved )
	{
		System.out.println( new Medved().begin() );
	}
}

class Medved extends Preved
{
	int test;
	
	public int begin()
	{
		Belka test;
		
		preved=new Belka();
		test=preved.test( );
		return 0;
	}
}

class Preved
{
	Belka preved;
}

class Belka extends Krot
{
}

class Krot
{
	public Preved test()
	{
		return new Medved();
	}
}
