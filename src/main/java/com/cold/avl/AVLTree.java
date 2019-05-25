package com.cold.avl;

import java.util.Comparator;

public class AVLTree<T> {

    private TreeNode<T> root;

    private Comparator<T> comparator;

    private int treesZie;

    private AVLTree(Comparator<T> comparator){
        this.comparator = comparator;
    }

    public synchronized void insert(T node){
        root =TreeNode.insert(root,node, comparator, treesZie);
    }

    public synchronized void delete(T node){
        if(root != null){
             root=TreeNode.delete(root,node, comparator,treesZie);
        }
    }

    public int getSize(){
        return treesZie;
    }

    public void preOrder(){
        this.root.preOrder();
    }

    private static class TreeNode<T>{
        private TreeNode<T> left= null;
        private TreeNode<T> right=null;
        private int depth=0;
        private T value;

        public boolean isLeafe(){
            return left==null && right == null;
        }

        private TreeNode(T value){
            this.value = value;
            this.left = this.right =null;
            this.depth=1;
        }

        private static <G>  TreeNode<G> insert(TreeNode<G> treeNode, G value, Comparator<G> comparator, int size){

            if(treeNode == null){
                size++;
                return new TreeNode<>(value);
            }

            int compare = comparator.compare(value, treeNode.value);
            if(compare ==0){
               return treeNode;
            }
            else if(compare <0 ){
               treeNode.left= insert(treeNode.left,value, comparator,size);
            }else{
                treeNode.right = insert(treeNode.right,value, comparator,size);
            }
            treeNode.setDepth(treeNode.getMaxChildDepth()+1);
            int depthDiff = treeNode.depthDifference();
            if(depthDiff>1){
                if(comparator.compare(value,treeNode.left.getValue())<0){
                    return rightRotate(treeNode);
                }else{
                    treeNode.left = leftRotate(treeNode.left);
                    return rightRotate(treeNode);
                }
            }else if(depthDiff <-1){
                if(comparator.compare(value,treeNode.right.getValue())>0){
                    return leftRotate(treeNode);
                }else{
                    treeNode.right = rightRotate(treeNode.right);
                    return leftRotate(treeNode);
                }
            }
            return treeNode;
        }

        private static <G>  TreeNode<G> delete(TreeNode<G> treeNode, G value, Comparator<G> comparator, int treeSize){

            if(treeNode == null){
                return treeNode;
            }

            int compare = comparator.compare(value, treeNode.value);
            if(compare <0 ){
                treeNode.left= delete(treeNode.left,value, comparator,treeSize);
            }else if(compare >0){
                treeNode.right = delete(treeNode.right,value, comparator,treeSize);
            }else{
                if(treeNode.left == null || treeNode.right ==null){
                    TreeNode<G> temp =null;
                    if(treeNode.left == null){
                        temp = treeNode.right;
                    }else{
                        temp = treeNode.left;
                    }

                    if(temp == null){
                        temp=treeNode;
                        treeNode=null;
                    }else{
                        treeNode = temp;
                    }
                    treeSize--;
                }else{
                    TreeNode<G> temp = getMinValueNode(treeNode.right);
                    treeNode.value= temp.value;
                    treeNode.right=delete(treeNode.right,treeNode.right.getValue(),comparator,treeSize);
                }
            }

            if(treeNode == null){
                return treeNode;
            }

            treeNode.setDepth(treeNode.getMaxChildDepth()+1);
            int depthDifference = treeNode.depthDifference();

            if(depthDifference >1){
                if(treeNode.right.getMaxChildDepth()>=0){
                    return rightRotate(treeNode);
                }else{
                    treeNode.left = leftRotate(treeNode.left);
                    return rightRotate(treeNode);
                }

            }else if(depthDifference <-1){
                if(treeNode.left.getMaxChildDepth()<=0){
                    return leftRotate(treeNode);
                }else{
                    treeNode.right = rightRotate(treeNode.right);
                    return leftRotate(treeNode);
                }
            }

            return treeNode;
        }


        private static <G> TreeNode<G> getMinValueNode(TreeNode<G> treeNode){
            if (treeNode == null) {
                return treeNode;
            }
            TreeNode<G> temp = treeNode;
            while(temp.left != null){
                temp = temp.left;
            }
            return temp;
        }

        private int getMaxChildDepth(){
            return Math.max(getDepth(left),getDepth(right));
        }

        private int getDepth() {
            return depth;
        }

        private void setDepth(int depth){
            this.depth =depth;
        }


        private int depthDifference(){
            return getDepth(this.left)-getDepth(this.right);
        }

        private static int getDepth(TreeNode treeNode){
            if(treeNode == null){
                return 0;
            }
            return treeNode.getDepth();
        }

        private boolean delete(TreeNode<T> treeNode){
            return false;
        }

        private T getValue(){
            return this.value;
        }

        private void preOrder(){
            System.out.print(getValue());
            System.out.println("");
            if(this.left != null)this.left.preOrder();
            if(this.right != null)this.right.preOrder();
        }

        private static<G> TreeNode<G> rightRotate(TreeNode<G> rootNode) {
            TreeNode<G> newRootNode = rootNode.left;
            TreeNode<G> tempNode = newRootNode.right;

            newRootNode.right = rootNode;
            rootNode.left = tempNode;

            rootNode.depth = rootNode.getMaxChildDepth()+1;
            newRootNode.depth = newRootNode.getMaxChildDepth()+1;

            return newRootNode;
        }


        private static<G> TreeNode<G> leftRotate(TreeNode<G> rootNode) {
            TreeNode<G> newRootNode = rootNode.right;
            TreeNode<G> tempNode = newRootNode.left;

            newRootNode.left = rootNode;
            rootNode.right = tempNode;

            rootNode.depth = rootNode.getMaxChildDepth()+1;
            newRootNode.depth = newRootNode.getMaxChildDepth()+1;

            return newRootNode;
        }

    }

    public static <G extends Comparable<G>> AVLTree<G> create(){
        Comparator<G> comparator = Comparator.naturalOrder();
        return new AVLTree<>(comparator);
    }

    public static <G> AVLTree<G> create(Comparator<G> comparator){
        return new AVLTree<>(comparator);
    }

    public static void main(String[] args) {
        AVLTree<Integer> tree = AVLTree.create();

        /* Constructing tree given in the above figure */
        tree.insert(10);
        tree.insert( 20);
        tree.insert(30);
        tree.insert(40);
        tree.insert(50);
        tree.insert(25);

        /* The constructed AVL Tree would be
             30
            /  \
          20   40
         /  \     \
        10  25    50
        */
        System.out.println("Preorder traversal" +
                " of constructed tree is : ");
        tree.preOrder();

        tree.delete(25);
        tree.delete(20);

        System.out.println("Preorder traversal" +
                " of constructed tree is : ");
        tree.preOrder();


        AVLTree<Integer> tree2 = AVLTree.create();

        /* Constructing tree given in the above figure */
        tree2.insert(9);
        tree2.insert(5);
        tree2.insert(10);
        tree2.insert(0);
        tree2.insert(6);
        tree2.insert(11);
        tree2.insert(-1);
        tree2.insert(1);
        tree2.insert(2);
        
         /* The constructed AVL Tree would be
        9
        / \
        1 10
        / \ \
        0 5 11
        / / \
        -1 2 6
        */
        System.out.println("Preorder traversal of "+
                "constructed tree is : ");
        tree.preOrder();

        tree.delete(10);

        /* The AVL Tree after deletion of 10
        1
        / \
        0 9
        /     / \
        -1 5 11
        / \
        2 6
        */
        System.out.println("");
        System.out.println("Preorder traversal after "+
                "deletion of 10 :");
        tree.preOrder();
    }
}
