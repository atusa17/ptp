package edu.msudenver.tsp.services.parser;

public class Node
{
    String statement;
    Node left;
    Node right;
    Node center;
    Node parent;
    int depth;

    public Node(final String s, final Node p)
    {
        statement = s + "\n";
        left = null;
        right = null;
        center = null;
        parent = p;
        if(p != null) depth = p.depth + 1;
        else depth = 0;
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