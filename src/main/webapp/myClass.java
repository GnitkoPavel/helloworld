public class myClass
{
    private static myObject1 myObj1;
    private static Object mutex1 = new Object();
    private static myObject2 myObj2;
    private static Object mutex2 = new Object();
    public static void updateObject1()
        {
            synchronized(mutex1)
            {
            System.out.println("Something1");
            }
        }
    public static void updateObject2()
    {
                                synchronized(mutex2)
        {
            System.out.println("Something2");
        }
    }

}

