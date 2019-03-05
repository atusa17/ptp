package edu.msudenver.tsp.services.parser;

public class Node
{
    String statement;
    Node left;
    Node right;
    Node center;
    Node parent;
    int depth;

    public Node(final String statement, final Node parent)
    {
        this.statement = statement + "\n";
        left = null;
        right = null;
        center = null;
        this.parent = parent;
        if(parent != null)
        {
            depth = parent.depth + 1;
        }
        else
        {
            depth = 0;
        }
    }

    @Override
    public String toString()
    {
        String returnVal = this.depth + ": " + this.statement;

        if(this.left != null) returnVal += "... " + this.left.toString();
        if(this.center != null) returnVal += "... " + this.center.toString();
        if(this.right != null) returnVal += "... " + this.right.toString();

        return returnVal;
    }
}