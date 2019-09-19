import java.util.*;

 public class AVLtree <T extends Comparable<T>>{
  class Node{

    int balanceF;
    T valor;
    int altura;
    Node left, right;
   boolean modified = true;

    public Node(T valor){
      this.valor = valor;
    }
  }
  private Node root,alt;
  private int contadorNodes = 0;

  public int altura(){
    if(root == null) return 0;
    return root.altura;
  }

  public int size(){
    return contadorNodes;
}

public int push(int i){
  return i;
}

  public boolean vacio(){
    return size()==0;
  }

  /*public void Imprimir(){

  }*/

  public boolean contains(T valor){
    return contains(root, valor);
  }

  private boolean contains(Node node, T valor){

    if(node == null) return false;

    int compara = valor.compareTo(node.valor);

    if(compara > 0) return contains(node.right, valor);

    if(compara < 0) return contains(node.left, valor);

    return true;

  }

  private void update(Node node){
    int nodalturaIzq = (node.left == null)?-1: node.left.altura;
    int nodalturaDere = (node.right == null)?-1: node.right.altura;

    node.altura = 1 + Math.max(nodalturaIzq,nodalturaDere);
    node.balanceF = nodalturaDere - nodalturaIzq;
  }

  public boolean insertar(T valor){
    if (valor == null )return false;
    if (!contains(root,valor)){
      root = insertar(root,valor);
      contadorNodes++;
      return true;
    }
    return false;
  }

  private Node insertar(Node node, T valor){

    if(node == null) return new Node(valor);

    int compara = valor.compareTo(node.valor);

    if (compara<0) {
      node.left = insertar(node.left,valor);
    }else{
      node.right = insertar(node.right,valor);
    }

    update(node);
    return balance(node);
  }

  private Node balance(Node node){
    if (node.balanceF == -2) {
      if (node.left.balanceF<=0){

        return izqLCaso(node);

      } else {
      return izqRCaso(node);
      }
    }else if (node.balanceF == +2) {
        if (node.right.balanceF >=0) {
          return dereRCaso(node);

        } else {
          return dereLCaso(node);
        }
      }
      return node;
  }

  private Node izqLCaso (Node node){
    return rightRot(node);
  }

  private Node izqRCaso(Node node){
    node.left = leftRot(node);
    return izqLCaso(node);
  }

  private Node dereLCaso(Node node){
    node.right = rightRot(node);
    return dereLCaso(node);
  }

  private Node dereRCaso(Node node){
      return leftRot(node);
  }

  private Node rightRot(Node node){
    Node padreNu = node.left;
    node.left = padreNu.right;
    padreNu.right = node;
    update(node);
    update(padreNu);
    return padreNu;
  }

  private Node leftRot(Node node){
    Node padreNu = node.right;
    node.right = padreNu.left;
    padreNu.left = node;
    update(node);
    update(padreNu);
    return padreNu;
  }

public boolean remover(T elemento){
  if (elemento == null) return false;
  if (contains(root,elemento)) {
    root = remove (root,elemento);
    contadorNodes--;
    return true;
  }
  return false;
}

private Node remove(Node node, T elemento){

  int compara = elemento.compareTo(node.valor);
  if (node == null) return null;

    if (compara < 0) {
      node.left = remove(node.left,elemento);
    } else if (compara > 0) {
      node.right = remove(node.right,elemento);
    } else{

      if (node.left == null) {
        return node.right;

      }else if (node.right == null) {

        return node.left;

      }else{
        if(node.left.altura>node.right.altura){

          T sigValue = max(node.left);
          node.valor = sigValue;
          node.left = remove (node.left,sigValue);

        }else{

           T sigValue = min(node.right);
           node.valor = sigValue;
           node.right = remove (node.right,sigValue);
        }
      }
    }

    update(node);
    return balance(node);
  }

  private T min(Node node){
    while(node.left != null)
      node = node.left;
      return node.valor;
  }

  private T max(Node node){
    while(node.right != null)
      node = node.right;
      return node.valor;
  }

  public int Naltura(int alt){
    if(root == null) return 0;
    return root.altura;
  }
/*
  public int prof(T elemento){
    if(elemnto == null) return 0;
    if(prof(elemento.right)>prof(elemento.left)) return prof(elemento.right);
    else
    return prof(elemento.left);
  }*/




}
