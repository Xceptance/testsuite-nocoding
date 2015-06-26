package tmp;



public class Test
{
    class AbstractClass {
        public final AbstractClass previous;
        public AbstractClass(final AbstractClass previous){
            this.previous = previous;
        }
        protected void print(){
            System.err.println("AbstractClass");
        }
        
    }
    class SubClass extends AbstractClass{
        public final SubClass previous;
        public SubClass(final SubClass previous){
            super(previous);
            this.previous = previous;
        }
        protected void print(){
            System.err.println("SubClass"); 
        }
    }
    class SubSubClass extends SubClass{
        
        public SubSubClass(final SubClass previous)
        {
            super(previous);
        }

        protected void print(){
            System.err.println("SubSubClass"); 
        }
    }
    public static void main(final String args[])
    {
        final Test t = new Test();
       final Test.AbstractClass ac = t.new SubClass(null);
       final Test.AbstractClass ac2 = t.new SubClass((SubClass) ac);
       final Test.AbstractClass ac3 = t.new SubSubClass((SubClass) ac2);
       final Test.AbstractClass ac4 = t.new SubSubClass((SubClass) ac3);
       ac4.previous.print();
       ac4.print();
       ac3.print();
    }
}
