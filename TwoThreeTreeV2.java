//2-3 obtenido (en parte) de github.com/devkapupara

import java.util.*;

class TwoThreeTreeV2<T extends Comparable<T> > implements Iterable
{
private Node root;
private int size;
private boolean modified = true;
private Traversal traverse;

TwoThreeTreeV2()
{
    root     = null;
    traverse = new Traversal();
    size     = 0;
}

void addAll(Collection<T> collection)
{
    for (T item: collection)
        this.insert(item);
    modified = true;
}

void insert(T x)
{
    if (root == null)                               // If root is null, then create a new root with null as the parent
    {
        root = new Node(x);
        size++;
    }
    root.insert(x);                                 // Otherwise induce a recursive insert.
    modified = true;
}

int size()
{
    return size;
}

boolean search(T x)
{
    if (root == null)                               // If the root is null, then tree doesn't exist -> return null
        return false;
    return root.search(x).keys.contains(x);
}

void clear()
{
    root = null;
    size = 0;
}

public Iterator<T> iterator()
{
    if (modified)                               // Build in-order Linked-List only if the tree was modified.
    {
        traverse.traverseTree();                // Traverse the tree
        modified = false;                       // Set flag to false since we just built a linked-list.
    }
    return traverse.ordered.iterator();         // Delegate the task of iterator to Linked-List's in built iterator.
}

public void printAscending()
{
    ArrayList<T> ordenado = new ArrayList<>();
    traverse.traverseTree();
    ordenado = traverse.ordered;
    for (int f = 0; f < ordenado.size(); f++)
    {
        System.out.print(ordenado.get(f) + " ");
    }

    System.out.println("");
}

public void printDescending()
{
    ArrayList<T> backwards = new ArrayList();
    traverse.traverseTree();
    backwards = traverse.ordered;
    for (int s = backwards.size() - 1; s > 0; s--)
    {
        System.out.print(backwards.get(s) + " ");
    }
    System.out.println("");
}

public void nodeDepth(Node n)
{
    traverse.traverseTree();
    int depth = 0;
    if (!root.children.contains(n))
    {
        depth = traverse.nodeDepth(root, 0, n);
    }
    System.out.println("La profundidad del nodo es: " + depth);
}

public void nodeHeight(Node n)
{
    traverse.traverseTree();
    int height = 0;
    if (!root.children.contains(n))
    {
        height = traverse.nodeDepth(root, 0, n);
    }
    System.out.println("La altura del nodo es: " + height);
}

//-----------------------------------------Inner Iterator Class-------------------------------------------------//

private class Traversal
{
ArrayList<T> ordered;
void traverseTree()
{
    ordered = new ArrayList<>();                           // Reset the ordered list. traverseTree will be only called in case of modification
    traverse(root);                                        // Initialize traversal from the root.
}


void traverse(Node n)
{
    if (n.children.size() == 0)                             // If it's a leaf node, add all its key to the linked list
        ordered.addAll(n.keys);
    else
    {
        traverse(n.children.get(0));                        // Otherwise, first traverse the left branch
        ordered.add(n.keys.get(0));                         // When it is done, create add key 1 of the node n to keep it ordered.
        traverse(n.children.get(1));                        // Then traverse the middle/right branch of n.
        if (n.children.size() == 3)                         // If there are 3 branches, then we still need to traverse it.
        {
            ordered.add(n.keys.get(1));                     // Before we traverse, add the 2nd key of n to the list since everything is going to be greater than it in the right branch.
            traverse(n.children.get(2));                    // Then traverse the last branch and add all encountered nodes in order.
        }
    }
}

Integer nodeDepth(Node n, int depth, Node destination)
{
    if (n.equals(destination) || n.children.get(0) == null)
    {
        return depth;
    }
    depth++;
    nodeDepth(n.children.get(0), depth, destination);     // Otherwise, first traverse the left branch

    nodeDepth(n.children.get(1), depth, destination);     // Then traverse the middle/right branch of n.
    if (n.children.size() == 3)                           // If there are 3 branches, then we still need to traverse it.
    {
        nodeDepth(n.children.get(2), depth, destination); // Then traverse the last branch and add all encountered nodes in order.
    }
    return 0;                                             //Unreachable, just for compiler
}
}

//-------------------------------------------INNER CLASS NODE---------------------------------------------------//
private class Node
{
ArrayList<T> keys        = new ArrayList<>(3);
ArrayList<Node> children = new ArrayList<>(4);

Node(T data)
{
    keys.add(data);
}

private void addKey(T x)
{
    this.keys.add(x);                                         // Insert x at the last position
    int lastIndex = this.keys.size() - 1;
    for (int i = 0; i < lastIndex; i++)                       // Repeatedly check the key at index i if its greater than key at index keySize
    {
        if (this.keys.get(i).compareTo(this.keys.get(lastIndex)) > 0)
        {
            T temp = this.keys.get(i);                        // If it is, swap them and increase the i
            this.keys.set(i, this.keys.get(lastIndex));
            this.keys.set(lastIndex, temp);
        }
    }
}

private boolean insert(T x)
{
    if (this.keys.contains(x))                                                       // If the node contains key, return false
        return false;

    int i = 0;
    while (i < this.keys.size() && x.compareTo(this.keys.get(i)) > 0)                // Find the correct child to insert at.
        i++;
    boolean childWasSplit;

    if (i < this.children.size())                                       // The node is not a leaf, so I can recursively insert.
        childWasSplit = this.children.get(i).insert(x);
    else                                                                // Its a leaf, just add the key and then check if it needs to split.
    {
        this.addKey(x);
        size++;                                                         // Key wasn't a duplicate a could be inserted in the tree, therefore increase the size.
        if (this.keys.size() == 3)
        {
            this.splitify();                                            // Split the node and return true to let the parent know
            return true;                                                // that child was split.
        }
        return false;
    }

    if (childWasSplit)
    {                                                             // Child was split and parent might be a 2-node or 3-node.
        Node tempChild = this.children.get(i);                    // Copy of the split child because it will get modified by overwriting parent's children
        this.addKey(tempChild.keys.get(0));                       // "Moving the child's key to the parent"
        this.children.set(i, tempChild.children.get(0));          // The current child is replaced by its left child
        this.children.add(i + 1, tempChild.children.get(1));      // Add the right child to the next index. ArrayList does the shifting automatically.
        if (this.children.size() == 4)                            // The node is really full, it was 3-node and now it became 4-node, so it
        {
            this.splitify();                                      // needs to redistribute its children by creating nodes out of itself.
            return true;
        }
    }                                                                   // It was a 2-node so the split child's children became parent's children and
    return false;                                                       // and it's key adjusted inside the parent so it became a 3-node.
}

private void splitify()
{
    Node left  = new Node(this.keys.get(0));
    Node right = new Node(this.keys.get(2));
    if (this.children.size() == 4)                              // If the node is overfull, balance everything
    {                                                           // by giving left-most two nodes to the left child
        left.children.add(this.children.get(0));                // and the right most two nodes to the right child.
        left.children.add(this.children.get(1));
        right.children.add(this.children.get(2));
        right.children.add(this.children.get(3));
    }
    this.children.clear();
    this.children.add(left);                                    // Set the new left and right child of "this"
    this.children.add(right);

    T tempKey = this.keys.get(1);                               // Also, the keys have been passed to the children
    this.keys.clear();                                          // so modify it. In the end, only the middle key should be
    this.keys.add(tempKey);                                     // there.
}

private Node search(T val)
{
    if (this.children.size() == 0 || this.keys.contains(val))                             // If the node is a leaf or has the key, return that node
        return this;
    else
    {
        int i = 0;
        while (i < this.keys.size() && val.compareTo(this.keys.get(i)) > 0)               // Else recursively search that appropriate branch by making use
            i++;                                                                          // of the index i.
        return this.children.get(i).search(val);
    }
}



public String toString()                    // toString method
{
    return this.keys.get(0) + (this.keys.size() == 2 ? " " + this.keys.get(1) : "");
}
}

public static void main(String[] args)
{
    TwoThreeTreeV2<Integer> set       = new TwoThreeTreeV2<Integer>();
    ArrayList<Integer>      elementos = new ArrayList<Integer>();
    int                     size      = 100000;
    Random                  rng       = new Random();
    Scanner                 lector    = new Scanner(System.in);

    set.insert(1);
    set.insert(7777);
    set.insert(89);
    set.insert(2333); //Demos de inserción
    set.insert(90909);
    set.insert(47509);
    set.insert(377592);
    set.insert(999999);
    set.insert(42);
    set.insert(576431);
    for (int s = 0; s < size; s++)
    {
        Integer rand = rng.nextInt(size * 10) + 1;
        elementos.add(rand);
        set.insert(rand);
    }


    System.out.println("");
    System.out.println("Escriba uno de los numeros siguientes para buscarlo: 1 \t 7777 \t 89 \t 2333 \t 90909 \t 47509 \t 377592 \t 999999 \t 42 \t 576431 \n");
    Integer searcher  = lector.nextInt();
    long    startTime = System.nanoTime();

    set.search(searcher); //Demo de función de búsqueda
    long endTime     = System.nanoTime();
    long timeElapsed = endTime - startTime;
    System.out.println("tiempo en milisegundos : 0.0" + timeElapsed / 10000); //si el resultado es 1,son 0.01 ms

    //set.printAscending();     //Funciones cuyo nombre las explica:

    //set.printDescending();






    for (int x = 0; x < set.root.children.size(); x++)
    {
        if (set.root.children.get(x) != null)
        {
            set.nodeDepth(set.root.children.get(0));    //GetDepth
        }
    }

    for (int x = 0; x < set.root.children.size(); x++)
    {
        if (set.root.children.get(x) != null)
        {
            set.nodeHeight(set.root.children.get(0));     //GetDepth
        }
    }

    set.clear();                                                        //Delete
    /*  for (int r = 0; r < found.size(); r++)
       {
          System.out.println(found.get(r) + " ");   //Por si se requiere comprobar que se encontraron
       }
     */
}
}
