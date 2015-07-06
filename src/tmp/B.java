package tmp;

public class B extends A
{
    @Override
    protected void shout()
    {
        System.err.println("B");
    }

    public static void main(final String args[])
    {
        final B b = new B();
        b.doShout();
    }
}
