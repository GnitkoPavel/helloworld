public class myClass
{
    Arrays.asList("a1", "a2", "b1", "c2", "c1").stream()
    .filter(...)
    .forEach(...);

    Arrays.asList(1, 2, 3, 4).stream() // Noncompliant
    .filter(...)
    .forEach(...);
    private static myObject1 myObj1;
    private static mutex1 = new Object();
    private static myObject2 myObj2;
    private static mutex2 = new Object();
    public static void updateObject1()
        {
            synchronized(mutex1)
            {
            System.out.println("Something1")
            }
        }
    public static void updateObject2()
    {
                                synchronized(mutex2)
        {
            System.out.println("Something2")
        }
    }

}

