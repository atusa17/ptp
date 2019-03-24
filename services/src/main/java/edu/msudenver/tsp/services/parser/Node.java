package edu.msudenver.tsp.services.parser;

import lombok.Getter;
import lombok.Setter;

public class Node
{
    @Getter private final String statement;
    @Getter @Setter private Node left;
    @Getter @Setter private Node right;
    @Getter @Setter private Node center;
    @Getter private final Node parent;
    @Getter private final int depth;

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