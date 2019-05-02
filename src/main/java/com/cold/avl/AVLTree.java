package com.cold.avl;
import java.util.Comparator;

public class AVLTree<T> {

    private TreeNode<T> root;

    private Comparator<T> comparator;

    private int treesZie;

    private AVLTree(Comparator<T> comparator){
        this.comparator = comparator;
    }

    public void insert(T node){
        if(root == null){
            root = new TreeNode<>(node,comparator);
        }else{
            root.insert(new TreeNode<>(node,comparator));
        }
        treesZie++;
    }

    public boolean delete(T node){
        if(root != null){
             if(root.delete(new TreeNode<>(node,comparator))){
                 treesZie --;
                 return true;
             }
        }
        return false;
    }

    public int getSize(){
        return treesZie;
    }

    private static class TreeNode<T>{
        private TreeNode<T> left= null;
        private TreeNode<T> right=null;
        private final Comparator<T> comparator;
        private T value;

        private TreeNode(T value, Comparator<T> comparator){
            this.value = value;
            this.left = this.right =null;
            this.comparator = comparator;
        }

        private int insert(TreeNode<T> treeNode){
            //TODO : add the re-balancinglogic
            int compare = this.comparator.compare(this.value, treeNode.getValue());
            if(compare ==0){
                this.value = treeNode.value;
                return 0;
            }
            else if(compare <0 ){
                if(this.left == null){
                    this.left = treeNode;
                }else{
                    this.left.insert(treeNode);
                }
            }else{
                if(this.right == null){
                    this.right = treeNode;
                }else{
                    this.right.insert(treeNode);
                }
            }

        }

        private boolean delete(TreeNode<T> treeNode){
            return false;
        }

        private T getValue(){
            return this.value;
        }

    }

    public static <G extends Comparable<G>> AVLTree<G> create(Class<G> clazz){
        return new AVLTree<>(Comparator.naturalOrder());
    }

    public static <G> AVLTree<G> create(Comparator<G> comparator){
        return new AVLTree<>(comparator);
    }
}
