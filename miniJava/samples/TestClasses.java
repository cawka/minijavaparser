class TestClasses {
	public static void main( String[] preved )
	{
		System.out.println( new Medved().begin() );
	}
}

class Medved extends Preved
{
	public int begin()
	{
		preved=new Belka();
		preved=this.test( );
		return 0;
	}
}

class Preved
{
	Krot preved;

	public Krot test()
	{
		return new Krot();
	}
}

class Belka extends Krot
{
}

class Krot
{
}
