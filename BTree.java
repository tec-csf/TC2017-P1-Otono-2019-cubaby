//parte del código fue obtenido de https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/BTree.java.html
//https://stackoverflow.com/questions/6890580/implementing-a-b-tree-in-java-using-generics

import java.io.*;
import java.util.*;


 class BTree<T extends Comparable<T>> {

    private static BTree<Integer> tree = new BTree<Integer>();//programción genérica

    private static BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));

    public static void main(String args[]) throws IOException {

      Scanner reader = new Scanner(System.in);


        int opc =0;
        int valor =0;

        for (int i =0;i<10 ; i++ ) {
            int p = (int)(Math.random()*100+1);
            tree.insert(p);
        }

        System.out.println("Alrbol B creado...");

        while (opc<5) {
          System.out.println("Elija una opción");
          System.out.println("1.- Insertar");
          System.out.println("2.- Borrar");
          System.out.println("3.- Buscar");
          System.out.println("4.- Imprimir");
          System.out.println("5.- Altura y profundidad");
          System.out.println("6.- Salir");
          opc = reader.nextInt();

          switch (opc) {

            case 1 :
            System.out.println("Valor a insertar");
            valor =  reader.nextInt();
            tree.insert(valor);
            break;

            case 2:
            System.out.println("Valor a borrar");
            valor =  reader.nextInt();
            System.out.println("Árbol antes:"+tree.toString());
            tree.delete(valor);
            System.out.println("Árbol después:"+tree.toString());
            break;

            case 3:
            System.out.println("Valor a buscar");
            valor =  reader.nextInt();
            if (tree.isMember(valor)) {
              System.out.println("Valor encontrado");
            }else{
              System.out.println("Valor NO enocntrado");
            }
            break;

            case 4:
            System.out.println("Árbol:"+tree.toString());
            System.out.println("Árbol en PREOrden:"+tree.preOrder());

            break;

            case 5:
            System.out.println("Árbol altura:"+tree.getHeight());
            System.out.println("Nivel de Nodo:"+tree.getHeight());
            break;

            case 6:
            break;

          }

        }

    }

    private static String stringInput(String inputRequest) throws IOException {
        System.out.println(inputRequest);
        return reader.readLine();
    }



    private Node root;

    public BTree() {
        root = null;
    }


    public BTree(Node root) {
        this.root = root;
    }


    public void insert(T info) {
        insert(info, root, null, false);
    }


    public boolean isMember(T info) {
        return isMember(info, root);
    }

    public void delete(T info) {
        delete(info, root);
    }


    public String toString() {
        return inOrder();
    }


    public String inOrder() {
        return inOrder(root);
    }

    public String preOrder() {
        return preOrder(root);
    }


    public int getHeight() {
        return getHeight(root);
    }

    private void insert(T info, Node node, Node parent, boolean right) {

        if (node == null) {
            if (parent == null) {
                root = node = new Node(info, parent);
            } else if (right) {
                parent.right = node = new Node(info, parent);
            } else {
                parent.left = node = new Node(info, parent);
            }
            restructInsert(node, false);
        } else if (info.compareTo(node.information) == 0) {
            node.information = info;
        } else if (info.compareTo(node.information) > 0) {
            insert(info, node.right, node, true);
        } else {
            insert(info, node.left, node, false);
        }
    }

    private boolean isMember(T info, Node node) {

        boolean member = false;

        if (node == null) {
            member = false;
        } else if (info.compareTo(node.information) == 0) {
            member = true;
        } else if (info.compareTo(node.information) > 0) {
            member = isMember(info, node.right);
        } else {
            member = isMember(info, node.left);
        }

        return member;
    }

    private void delete(T info, Node node) throws NoSuchElementException {

        if (node == null) {
            throw new NoSuchElementException();
        } else if (info.compareTo(node.information) == 0) {
            deleteNode(node);
        } else if (info.compareTo(node.information) > 0) {
            delete(info, node.right);
        } else {
            delete(info, node.left);
        }
    }

    private void deleteNode(Node node) {

        Node eNode, minMaxNode, delNode = null;
        boolean rightNode = false;

        if (node.isLeaf()) {
            if (node.parent == null) {
                root = null;
            } else if (node.isRightNode()) {
                node.parent.right = null;
                rightNode = true;
            } else if (node.isLeftNode()) {
                node.parent.left = null;
            }
            delNode = node;
        } else if (node.hasLeftNode()) {
            minMaxNode = node.left;
            for (eNode = node.left; eNode != null; eNode = eNode.right) {
                minMaxNode = eNode;
            }
            delNode = minMaxNode;
            node.information = minMaxNode.information;

            if (node.left.right != null) {
                minMaxNode.parent.right = minMaxNode.left;
                rightNode = true;
            } else {
                minMaxNode.parent.left = minMaxNode.left;
            }

            if (minMaxNode.left != null) {
                minMaxNode.left.parent = minMaxNode.parent;
            }
        } else if (node.hasRightNode()) {
            minMaxNode = node.right;
            delNode = minMaxNode;
            rightNode = true;

            node.information = minMaxNode.information;

            node.right = minMaxNode.right;
            if (node.right != null) {
                node.right.parent = node;
            }
            node.left = minMaxNode.left;
            if (node.left != null) {
                node.left.parent = node;
            }
        }
        restructDelete(delNode.parent, rightNode);
    }

    private int getHeight(Node node) {
        int height = 0;

        if (node == null) {
            height = -1;
        } else {
            height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        }
        return height;
    }

    private String inOrder(Node node) {

        String result = "";
        if (node != null) {
            result = result + inOrder(node.left) + " ";
            result = result + node.information.toString();
            result = result + inOrder(node.right);
        }
        return result;
    }

    private String preOrder(Node node) {

        String result = "";
        if (node != null) {
            result = result + node.information.toString() + " ";
            result = result + preOrder(node.left);
            result = result + preOrder(node.right);
        }
        return result;
    }



    private void restructInsert(Node node, boolean wasRight) {

        if (node != root) {
            if (node.parent.balance == '_') {
                if (node.isLeftNode()) {
                    node.parent.balance = '/';
                    restructInsert(node.parent, false);
                } else {
                    node.parent.balance = '\\';
                    restructInsert(node.parent, true);
                }
            } else if (node.parent.balance == '/') {
                if (node.isRightNode()) {
                    node.parent.balance = '_';
                } else {
                    if (!wasRight) {
                        rotateRight(node.parent);
                    } else {
                        doubleRotateRight(node.parent);
                    }
                }
            } else if (node.parent.balance == '\\') {
                if (node.isLeftNode()) {
                    node.parent.balance = '_';
                } else {
                    if (wasRight) {
                        rotateLeft(node.parent);
                    } else {
                        doubleRotateLeft(node.parent);
                    }
                }
            }
        }
    }
// después de borrar un nodo se vulve a balancear
    private void restructDelete(Node z, boolean wasRight) {

        Node parent;
        boolean isRight = false;
        boolean climb = false;
        boolean canClimb;

        if (z == null) {
            return;
        }

        parent = z.parent;
        canClimb = (parent != null);

        if (canClimb) {
            isRight = z.isRightNode();
        }

        if (z.balance == '_') {
            if (wasRight) {
                z.balance = '/';
            } else {
                z.balance = '\\';
            }
        } else if (z.balance == '/') {
            if (wasRight) {
                if (z.left.balance == '\\') {
                    doubleRotateRight(z);
                    climb = true;
                } else {
                    rotateRight(z);
                    if (z.balance == '_') {
                        climb = true;
                    }
                }
            } else {
                z.balance = '_';
                climb = true;
            }
        } else {
            if (wasRight) {
                z.balance = '_';
                climb = true;
            } else {
                if (z.right.balance == '/') {
                    doubleRotateLeft(z);
                    climb = true;
                } else {
                    rotateLeft(z);
                    if (z.balance == '_') {
                        climb = true;
                    }
                }
            }
        }

        if (canClimb && climb) {
            restructDelete(parent, isRight);
        }
    }
//rotación a la izquierda
    private void rotateLeft(Node a) {

        Node b = a.right;

        if (a.parent == null) {
            root = b;
        } else {
            if (a.isLeftNode()) {
                a.parent.left = b;
            } else {
                a.parent.right = b;
            }
        }

        a.right = b.left;
        if (a.right != null) {
            a.right.parent = a;
        }

        b.parent = a.parent;
        a.parent = b;
        b.left = a;

        if (b.balance == '_') {
            a.balance = '\\';
            b.balance = '/';
        } else {
            a.balance = '_';
            b.balance = '_';
        }
    }
//rotación a la derecha
    private void rotateRight(Node a) {

        Node b = a.left;

        if (a.parent == null) {
            root = b;
        } else {
            if (a.isLeftNode()) {
                a.parent.left = b;
            } else {
                a.parent.right = b;
            }
        }

        a.left = b.right;
        if (a.left != null) {
            a.left.parent = a;
        }

        b.parent = a.parent;
        a.parent = b;
        b.right = a;

        if (b.balance == '_') {
            a.balance = '/';
            b.balance = '\\';
        } else {
            a.balance = '_';
            b.balance = '_';
        }
    }
//doble rotación
    private void doubleRotateLeft(Node a) {

        Node b = a.right;
        Node c = b.left;

        if (a.parent == null) {
            root = c;
        } else {
            if (a.isLeftNode()) {
                a.parent.left = c;
            } else {
                a.parent.right = c;
            }
        }

        c.parent = a.parent;

        a.right = c.left;
        if (a.right != null) {
            a.right.parent = a;
        }
        b.left = c.right;
        if (b.left != null) {
            b.left.parent = b;
        }

        c.left = a;
        c.right = b;

        a.parent = c;
        b.parent = c;

        if (c.balance == '/') {
            a.balance = '_';
            b.balance = '\\';
        } else if (c.balance == '\\') {
            a.balance = '/';
            b.balance = '_';
        } else {
            a.balance = '_';
            b.balance = '_';
        }

        c.balance = '_';
    }

    private void doubleRotateRight(Node a) {

        Node b = a.left;
        Node c = b.right;

        if (a.parent == null) {
            root = c;
        } else {
            if (a.isLeftNode()) {
                a.parent.left = c;
            } else {
                a.parent.right = c;
            }
        }

        c.parent = a.parent;

        a.left = c.right;
        if (a.left != null) {
            a.left.parent = a;
        }
        b.right = c.left;
        if (b.right != null) {
            b.right.parent = b;
        }

        c.right = a;
        c.left = b;

        a.parent = c;
        b.parent = c;

        if (c.balance == '/') {
            b.balance = '_';
            a.balance = '\\';
        } else if (c.balance == '\\') {
            b.balance = '/';
            a.balance = '_';
        } else {
            b.balance = '_';
            a.balance = '_';
        }
        c.balance = '_';
    }
//clase del nodo
    class Node {

        T information;

        Node parent;

        Node left;

        Node right;

        char balance;

        public Node(T information, Node parent) {
            this.information = information;
            this.parent = parent;
            this.left = null;
            this.right = null;
            this.balance = '_';
        }

        boolean isLeaf() {
            return ((left == null) && (right == null));
        }

        boolean isNode() {
            return !isLeaf();
        }

        boolean hasLeftNode() {
            return (null != left);
        }

        boolean hasRightNode() {
            return (right != null);
        }

        boolean isLeftNode() {
            return (parent.left == this);
        }

        boolean isRightNode() {
            return (parent.right == this);
        }
    }
}
