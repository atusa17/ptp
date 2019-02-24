package edu.msudenver.tsp.services.parser;

import edu.msudenver.tsp.persistence.controller.DefinitionController;
import edu.msudenver.tsp.persistence.controller.NotationController;
import edu.msudenver.tsp.persistence.controller.ProofController;
import edu.msudenver.tsp.persistence.controller.TheoremController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
class ParserService {
    private final DefinitionController definitionController;
    private final TheoremController theoremController;
    private final NotationController notationController;
    private final ProofController proofController;

    @Autowired
    public ParserService(final DefinitionController definitionController, final TheoremController theoremController,
                         final NotationController notationController, final ProofController proofController) {
        this.definitionController = definitionController;
        this.theoremController = theoremController;
        this.notationController = notationController;
        this.proofController = proofController;
    }

    //------------------------------------------------

    public void drive(final String userInput){
        final Node tree;
        ArrayList<String> statements = new ArrayList<String>();

        tree = parseRawInput(userInput);
        statements = retrieveStatements(tree);
        // retrieve statements from tree
        retrieveDefinitions(statements);
        // query databases for each part
    }

    //------------------------------------------------

    public class Node
    {
        String stmt;
        Node left, right, center, parent;
        int depth;

        //---------------------------------------------

        public Node(final String s, final Node p)
        {
            stmt = s + "\n";
            left = null;
            right = null;
            center = null;
            parent = p;
            if(p != null) depth = p.depth + 1;
            else depth = 0;
        }

        //---------------------------------------------

        public String toString()
        {
            String returnVal = this.depth + ": " + this.stmt;

            if(this.left != null) returnVal += "... " + this.left.toString();
            if(this.center != null) returnVal += "... " + this.center.toString();
            if(this.right != null) returnVal += "... " + this.right.toString();

            return returnVal;
        }
    }

    //------------------------------------------------

    Node root;

    //------------------------------------------------

    public Node parseRawInput(String input)
    {
        // convert to the same case for easier processing
        input = input.toLowerCase();

        root = new Node(input, null);

        // special case: nothing is submitted
        if(input.equals(""))
        {
            return root;
        }

        // check for periods at the end of the input
        if(input.charAt(input.length()-1) == '.')
        {
            input = input.substring(0, input.length()-1);
        }


        recurse(root);

        return root;
    }

    //------------------------------------------------

    private void recurse(final Node current)
    {
        int iContain;

        if(current.stmt.contains("let"))
        {
            current.left = new Node("let",
                    current);
            iContain = (current.stmt.contains("if") ? current.stmt.indexOf("if") : current.stmt.length());

            current.left.center = new Node(current.stmt.substring(current.stmt.indexOf("let")+3,
                    iContain),
                    current.left);
            recurse(current.left.center);
        }


        if(current.stmt.contains("if"))
        {
            current.center = new Node("if",
                    current);
            iContain = (current.stmt.contains("then") ? current.stmt.indexOf("then") : current.stmt.length());

            current.center.center = new Node(current.stmt.substring(current.stmt.indexOf("if")+2,
                    iContain),
                    current.center);
            recurse(current.center.center);
        }


        if(current.stmt.contains("then"))
        {
            current.right = new Node("then",
                    current);
            current.right.center = new Node(current.stmt.substring(current.stmt.indexOf("then")+4),
                    current.right);
            recurse(current.right.center);
        }
    }

    //------------------------------------------------

    public ArrayList<String> retrieveStatements(final Node tree)
    {
        ArrayList<String> stmts = new ArrayList<>();

        stmts = traverse(tree, stmts);

        return stmts;
    }

    private ArrayList<String> traverse(final Node node, final ArrayList<String> stmts)
    {
        if(node == null) return stmts;

        if(!(node.stmt.contains("let") || node.stmt.contains("if") || node.stmt.contains("then")))
        {
            stmts.add(node.stmt.trim());
            /*
            String[] withoutNewLines = node.stmt.split("\n");
            for(String line : withoutNewLines)
            {
                if(!line.equals("")) stmts.add(node.stmt);
            }
            */
        }

        traverse(node.left, stmts);

        traverse(node.center, stmts);

        traverse(node.right, stmts);

        return stmts;
    }

    public void retrieveDefinitions(final ArrayList<String> list)
    {

    }
}
