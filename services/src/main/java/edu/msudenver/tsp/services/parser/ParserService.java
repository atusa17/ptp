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
    Node root;

    @Autowired
    public ParserService(final DefinitionController definitionController, final TheoremController theoremController,
                         final NotationController notationController, final ProofController proofController) {
        this.definitionController = definitionController;
        this.theoremController = theoremController;
        this.notationController = notationController;
        this.proofController = proofController;
    }

    public void driveParsingProcess(final String userInput)
    {
        final Node tree;
        ArrayList<String> statements = new ArrayList<>();

        tree = parseRawInput(userInput);
        statements = retrieveStatements(tree);
        retrieveDefinitions(statements);
    }

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

        // eliminate periods at the end of the input
        if(input.charAt(input.length()-1) == '.')
        {
            input = input.substring(0, input.length()-1);
        }

        recurse(root);

        return root;
    }

    private void recurse(final Node current)
    {
        int iContain;

        if(current.statement.contains("let"))
        {
            current.left = new Node("let",
                    current);
            iContain = (current.statement.contains("if") ? current.statement.indexOf("if") : current.statement.length());

            current.left.center = new Node(current.statement.substring(current.statement.indexOf("let")+3,
                    iContain),
                    current.left);
            recurse(current.left.center);
        }


        if(current.statement.contains("if"))
        {
            current.center = new Node("if",
                    current);
            iContain = (current.statement.contains("then") ? current.statement.indexOf("then") : current.statement.length());

            current.center.center = new Node(current.statement.substring(current.statement.indexOf("if")+2,
                    iContain),
                    current.center);
            recurse(current.center.center);
        }


        if(current.statement.contains("then"))
        {
            current.right = new Node("then",
                    current);
            current.right.center = new Node(current.statement.substring(current.statement.indexOf("then")+4),
                    current.right);
            recurse(current.right.center);
        }
    }

    public ArrayList<String> retrieveStatements(final Node parsedTree)
    {
        ArrayList<String> statementList = new ArrayList<>();

        statementList = traverse(parsedTree, statementList);

        return statementList;
    }

    private ArrayList<String> traverse(final Node node, final ArrayList<String> statementList)
    {
        if(node == null) return statementList;

        if(!(node.statement.contains("let") || node.statement.contains("if") || node.statement.contains("then")))
        {
            statementList.add(node.statement.trim());
        }

        traverse(node.left, statementList);

        traverse(node.center, statementList);

        traverse(node.right, statementList);

        return statementList;
    }

    public void retrieveDefinitions(final ArrayList<String> list)
    {
        // stub
    }
}
