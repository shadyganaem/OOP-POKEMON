import ex2.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class myWGraph_DSTest
{

    private static DWGraph_DS g;
    private static int n;
    private static ArrayList<Integer> keys;
    private static Random rand;

    private static DecimalFormat df2 = new DecimalFormat("#.##");


    @BeforeAll
    static void setup()
    {
        g = new DWGraph_DS();
        n = 10;
        keys = new ArrayList<Integer>();
        rand = new Random(1);

        //createWGraph(n,n/2);

    }

    @Test
    public void runTimeTest()
    {
        long start = System.currentTimeMillis();

        createWGraph(100000,100000); // did in 16 sec.

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;

        if(timeElapsed > 60000)
            fail();

    }

    @Test
    public void addNode()
    {
        createWGraph(1000,1000);

        int t = (int) g.getV().stream().count();

        assertEquals(t, g.nodeSize());


    }

    @Test
    void getNode()
    {
        createWGraph(n,10*n);

        for(int i = 0; i < n; i++)
        {
            node_data node = g.getNode(i);
            assertNotEquals(null, node);
        }
        node_data node = g.getNode(n+1);  // no such key
        assertNull(node);

    }

    @Test
    void connect()
    {

        createWGraph(n,0);

        g.connect(0,1, 2.77);
        g.connect(6,0, 2.351);
        g.connect(7,6, 4.2);
        g.connect(8,8, 0.0);
        g.connect(4,5, 9.11);


        assertEquals(2.770, g.getEdge(0,1).getWeight(),0.001);
        assertEquals(2.351, g.getEdge(0,6).getWeight(),0.001);
        assertEquals(-1.0, g.getEdge(7,1).getWeight(),0.001);
        assertEquals(4.20, g.getEdge(7,6).getWeight(),0.001);
        assertEquals(0.0, g.getEdge(5,5).getWeight(),0.001);
        assertEquals(9.110, g.getEdge(4,5).getWeight(),0.001);

    }

    @Test
    void hasEdge()
    {
        createWGraph(n, 10*n);
        // only when seed = 1 !

        assertTrue(g.hasEdge(0,1));
        assertTrue(g.hasEdge(0,6));
        assertTrue(g.hasEdge(7,6));
        assertTrue(g.hasEdge(4,5));
        assertTrue(g.hasEdge(9,9));

        assertFalse(g.hasEdge(7, 1));

    }


    @Test
    void getV()
    {
        createWGraph(n, (int)(1.5*n));

        //System.out.println(" NODE_ID:   NEIGHBORS:");
        //printV();

    }

    @Test
    void getNeis()
    {
        createWGraph(n, (int)(1.5*n));
        int key = keys.get(rand.nextInt(g.nodeSize()));
        //System.out.println("NODE_ID:   NEIGHBORS:");
        //printV(g.getNode(key));

    }

    @Test
    void removeNode()
    {

        createWGraph(n, 10*n);

/*
        for(int key : keys)
        {
            System.out.print(key + " ");
        }
        System.out.println();
*/

        g.removeNode(0);
        // System.out.println(0 + "was removed.");

        g.removeNode(5);
        // System.out.println(5 + "was removed.");

        assertFalse(g.hasEdge(5,0));
        assertFalse(g.hasEdge(0,2));
        assertTrue(g.hasEdge(7,6));
        assertNull(g.getNode(5));

        for(int key : keys)
        {
            if(g.getNode(key) != null)
            {
                g.removeNode(key);
                // System.out.println(key + "was removed.");
            }

        }
        keys.removeAll(keys);

        g.getV().forEach(el -> System.out.println("\n" + el.getKey() + "was remain."));
        assertEquals(0, g.getV().size());

    }

    @Test
    void removeEdge()
    {
        createWGraph(n,10*n);

        g.removeEdge(0, 1);
        // System.out.println(0 + "<->" + 1 + " was removed.");

        g.removeEdge(0, 6);
        // System.out.println(0 + "<->" + 6 + " was removed.");

        assertFalse(g.hasEdge(0,6));
        assertFalse(g.hasEdge(0,1));
        assertTrue(g.hasEdge(7,6));

        ArrayList<Integer> neiKeys = new ArrayList<Integer>();

        for(int key : keys)
        {
            if(g.getNode(key) != null)
            {
                g.getE(key).forEach(el -> neiKeys.add(el.getDest()));

                for(int nei: neiKeys)
                {
                    g.removeEdge(key,nei);
                }
                // System.out.println(key + "<->" + nei.getKey() + " was removed.");
            }
            neiKeys.removeAll(neiKeys);
        }

        for(node_data node : g.getV())
        {
            assertEquals(0,g.getE(node.getKey()).size()); // only the node itself remains a neighbor.
        }

        // printV();
    }

    @Test
    void nodeSize()
    {
        int t = (int) g.getV().stream().count();

        assertEquals(t, g.nodeSize());
    }

    @Test
    void edgeSize()
    {
        int count = 0;
        for(node_data node : g.getV())
        {
            count = count + g.getE(node.getKey()).size();
        }

        assertEquals(count, g.edgeSize());
    }

    @Test
    void getMC()
    {
        createWGraph(n, 10*n);

        assertEquals(n+10*n, g.getMC());
    }

    /* ***************************** HELP FUNCTIONS ************************************* */

    static void createWGraph(int numOfNones, int numOfCon)
    {
        rand = new Random(1);
        g = new DWGraph_DS();
        keys = new ArrayList<Integer>();

        for(int i = 0; i < numOfNones; i++)
        {
            g.addNode(new node_data i);
            keys.add(i);
        }

        int key1;
        int key2;
        double w;

        for(int i = 0; i < numOfCon; i++)
        {
            key1 = keys.get(rand.nextInt(numOfNones));
            key2 = keys.get(rand.nextInt(numOfNones));
            w = 15.0* rand.nextDouble();

            g.connect(key1, key2, w);
        }

    }

    static void printV()
    {
        Iterator<node_info> it1 = g.getV().iterator();

        while(it1.hasNext())
        {
            node_info node = it1.next();
            System.out.print("\n" + node.getKey());

            Iterator<node_info> it2 = g.getV(node.getKey()).iterator();
            System.out.print(" ----->");

            while(it2.hasNext())
            {
                node_info nei = it2.next();
                System.out.print(" " + nei.getKey() + "(" + df2.format(g.getEdge(nei.getKey(),node.getKey())) + ")" + ",");
            }
        }

    }

    static void printV(node_info node)
    {
        Iterator<node_info> it = g.getV(node.getKey()).iterator();
        System.out.print(node.getKey() + " ----->");

        while(it.hasNext())
        {
            node_info nei = it.next();
            System.out.print(" " + nei.getKey() + "(" + df2.format(g.getEdge(nei.getKey(),node.getKey())) + ")" + ",");
        }
    }

}