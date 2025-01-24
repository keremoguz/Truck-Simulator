
// created Node class to design the structure of the AVL Tree Node
class Node
{
    ParkingLot element; // parking lot will be kept in the node as an element
    int h;  //for height
    Node leftChild;
    Node rightChild;

    //default constructor to create null node
    public Node()
    {
        leftChild = null;
        rightChild = null;
        element = null;
        h = 0;
    }
    // parameterized constructor
    public Node(ParkingLot element)
    {
        leftChild = null;
        rightChild = null;
        this.element = element;
        h = 0;
    }
}

class MyAvlTree
{
    private Node rootNode;
    public Node getRootNode(){
        return rootNode;
    }

    //Constructor to set null value to the rootNode
    public MyAvlTree()
    {
        rootNode = null;
    }

    public void insertElement(ParkingLot element)
    {
        rootNode = insertElement(element, rootNode);
    }

    //create insertElement() method to insert data in the AVL Tree recursively
    private Node insertElement(ParkingLot element, Node node)
    {

        if (node == null) node = new Node(element);

        // Check if the given element's capacity constraint is less than the current node's element's capacity
        // This means the element should be inserted into the left subtree
        else if (element.getCapacityConstraint() < node.element.getCapacityConstraint())
        {
            // Recursively call insertElement to find the correct position in the left subtree
            node.leftChild = insertElement(element, node.leftChild);

            // Check for imbalance in the tree after insertion in the left subtree
            if( getHeight(node.leftChild) - getHeight(node.rightChild ) == 2 )
                if( element.getCapacityConstraint() < node.leftChild.element.getCapacityConstraint())
                    node = rotateWithLeftChild(node);
                else
                    node = doubleWithLeftChild(node);
        }
        // If the given element's capacity constraint is greater than the current node's element's capacity
        // This means the element should be inserted into the right subtree
        else if(element.getCapacityConstraint() > node.element.getCapacityConstraint())
        {
            // Recursively call insertElement to find the correct position in the right subtree
            node.rightChild = insertElement(element, node.rightChild);
            if(getHeight( node.rightChild) - getHeight(node.leftChild) == 2 )
                if(element.getCapacityConstraint() > node.rightChild.element.getCapacityConstraint())
                    node = rotateWithRightChild(node);
                else
                    node = doubleWithRightChild(node);
        }
        else
            ;  // if the element is already present in the tree, we will do nothing
        node.h = getMaxHeight(getHeight( node.leftChild ), getHeight(node.rightChild)) + 1;

        return node;

    }

    //create getHeight() method to get the height of the AVL Tree
    private int getHeight(Node node )
    {
        if(node == null){
            return -1;
        }
        else return node.h;
    }

    //create getMaxHeight() method to get the maximum height from left and right node
    private int getMaxHeight(int leftNodeHeight, int rightNodeHeight)
    {
        if(leftNodeHeight>rightNodeHeight){
            return leftNodeHeight;
        }
        else{
            return rightNodeHeight;
        }
    }

    // creating rotateWithLeftChild() method to perform rotation of AVL tree node with left child
    private Node rotateWithLeftChild(Node node2) {
        Node node1 = node2.leftChild; // Set node1 as the left child of node2
        // Move the right subtree of node1 to the left subtree of node2
        node2.leftChild = node1.rightChild;
        // Place node2 as the right child of node1
        node1.rightChild = node2;
        // Update the height of node2 based on its new children
        node2.h = getMaxHeight(getHeight(node2.leftChild), getHeight(node2.rightChild)) + 1;
        // Update the height of node1 based on its new children
        node1.h = getMaxHeight(getHeight(node1.leftChild), node2.h) + 1;

        // Return node1 as the new root of this rotated subtree
        return node1;
    }

    // creating rotateWithRightChild() method to perform rotation of AVL tree node with right child
    private Node rotateWithRightChild(Node node1) {
        Node node2 = node1.rightChild; // Set node2 as the right child of node1
        // Move the left subtree of node2 to the right subtree of node1
        node1.rightChild = node2.leftChild;
        // Place node1 as the left child of node2
        node2.leftChild = node1;
        // Update the height of node1 based on its new children
        node1.h = getMaxHeight(getHeight(node1.leftChild), getHeight(node1.rightChild)) + 1;
        // Update the height of node2 based on its new children
        node2.h = getMaxHeight(getHeight(node2.rightChild), node1.h) + 1;

        // Return node2 as the new root of this rotated subtree
        return node2;
    }

    //create doubleWithLeftChild() method to perform double rotation of AVL tree node.
    // This method first rotate the left child with its right child, and after that, node3 with the new left child
    private Node doubleWithLeftChild(Node node3)
    {
        node3.leftChild = rotateWithRightChild(node3.leftChild);
        return rotateWithLeftChild(node3);
    }

    //create doubleWithRightChild() method to perform double rotation of binary tree node.
    // This method first rotate the right child with its left child and after that node1 with the new right child
    private Node doubleWithRightChild(Node node1)
    {
        node1.rightChild = rotateWithLeftChild(node1.rightChild);
        return rotateWithRightChild(node1);
    }

    //created searchElement() method to find an element in the AVL Tree

    public ParkingLot searchElement(long capacityConstraint)
    {
        return searchElement(rootNode, capacityConstraint);
    }

    private ParkingLot searchElement(Node head, long capacityConstraint) {
        while (head != null) { // Traverse until reaching a null node
            // If the capacity constraint is less than the current node's constraint, go to the left child
            if (capacityConstraint < head.element.getCapacityConstraint()) {
                head = head.leftChild;
            }
            // If the capacity constraint is greater than the current node's constraint, go to the right child
            else if (capacityConstraint > head.element.getCapacityConstraint()) {
                head = head.rightChild;
            }
            // If the capacity constraint matches the current node's constraint, return the element
            else {
                return head.element; // Found the element
            }
        }
        return null; // Element not found, return null
    }


    // Method to delete an element from the AVL Tree
    public void deleteElement(long capacityConstraint) {
        rootNode = deleteElement(rootNode, capacityConstraint);
    }

    // Helper method to delete an element recursively
    private Node deleteElement(Node node, long capacityConstraint) {
        if (node == null) {
            return null; // Element not found, return null
        }
        // find the correct node to delete
        if (capacityConstraint < node.element.getCapacityConstraint()) {
            node.leftChild = deleteElement(node.leftChild, capacityConstraint);
        } else if (capacityConstraint > node.element.getCapacityConstraint()) {
            node.rightChild = deleteElement(node.rightChild, capacityConstraint);
        } else {
            // Node with the matching capacity constraint found, perform deletion

            // Case 1: Node has at most one child
            if (node.leftChild == null || node.rightChild == null) {
                if(node.leftChild != null){
                    node = node.leftChild;
                }
                // If only the right child exists, replace node with right child
                else{
                    node = node.rightChild;
                }
            } else {
                // Case 2: Node has two children
                // Find the in-order successor (smallest element in the right subtree)
                Node successor = findMin(node.rightChild);
                node.element = successor.element; // Copy the in-order successor's data to this node
                node.rightChild = deleteElement(node.rightChild, successor.element.getCapacityConstraint());
            }
        }

        if (node == null) {
            return null; // If the tree had only one node
        }

        // Update the height of the current node
        node.h = getMaxHeight(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;

        // Rebalance the node if necessary
        int balance = getHeight(node.leftChild) - getHeight(node.rightChild);

        // Left-heavy case
        if (balance > 1) {
            if (getHeight(node.leftChild.leftChild) >= getHeight(node.leftChild.rightChild)) {
                return rotateWithLeftChild(node);
            } else {
                return doubleWithLeftChild(node);
            }
        }

        // Right-heavy case
        if (balance < -1) {
            if (getHeight(node.rightChild.rightChild) >= getHeight(node.rightChild.leftChild)) {
                return rotateWithRightChild(node);
            } else {
                return doubleWithRightChild(node);
            }
        }

        return node;
    }

    // Helper method to find the minimum node in a subtree
    private Node findMin(Node node) {
        while (node.leftChild != null) {
            node = node.leftChild;
        }
        return node;
    }
    private Node findMax(Node node){
        while (node.rightChild != null) {
            node = node.rightChild;
        }
        return node;
    }

}


