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
    private Node root;

    @Autowired
    public ParserService(final DefinitionController definitionController, final TheoremController theoremController,
                         final NotationController notationController, final ProofController proofController) {
        this.definitionController = definitionController;
        this.theoremController = theoremController;
        this.notationController = notationController;
        this.proofController = proofController;
    }

    public boolean parseUserInput(final String userInput)
    {
        try {
            final Node tree = parseRawInput(userInput);
            final ArrayList<String> statements = retrieveStatements(tree);

            return true;
        } catch(final Exception e) {
            return false;
        }
    }

    public Node parseRawInput(String input)
    {
        input = input.toLowerCase();

        root = new Node(input, null);

        if(input.equals(""))
        {
            return root;
        }

        recurse(root);

        return root;
    }

    private void recurse(final Node current)
    {
        int wordBeginsHere;

        if(current.statement.contains("let"))
        {
            current.left = new Node("let",
                    current);

            if(current.statement.contains("if")){
                wordBeginsHere = current.statement.indexOf("if");
            } else if(current.statement.contains("then")){
                wordBeginsHere = current.statement.indexOf("then");
            } else {
                wordBeginsHere = current.statement.length();
            }

            current.left.center = new Node(current.statement.substring(current.statement.indexOf("let")+"let".length(),
                    wordBeginsHere),
                    current.left);
            recurse(current.left.center);
        }


        if(current.statement.contains("if"))
        {
            current.center = new Node("if",
                    current);
            wordBeginsHere = (current.statement.contains("then") ? current.statement.indexOf("then") : current.statement.length());

            current.center.center = new Node(current.statement.substring(current.statement.indexOf("if")+"if".length(),
                    wordBeginsHere),
                    current.center);
            recurse(current.center.center);
        }


        if(current.statement.contains("then"))
        {
            current.right = new Node("then",
                    current);
            current.right.center = new Node(current.statement.substring(current.statement.indexOf("then")+"then".length()),
                    current.right);
            recurse(current.right.center);
        }
    }

    public ArrayList<String> retrieveStatements(final Node parsedTree)
    {
        final ArrayList<String> statementList = new ArrayList<>();

        traverse(parsedTree, statementList);

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
}
