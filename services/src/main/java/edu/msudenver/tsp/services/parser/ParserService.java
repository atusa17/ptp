package edu.msudenver.tsp.services.parser;

import edu.msudenver.tsp.persistence.controller.DefinitionController;
import edu.msudenver.tsp.persistence.controller.NotationController;
import edu.msudenver.tsp.persistence.controller.ProofController;
import edu.msudenver.tsp.persistence.controller.TheoremController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            final List<String> statements = retrieveStatements(tree);

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
        int startIndex;
        int endIndex;
        final String statement = current.getStatement();
        String nextStatement;

        if(statement.equals(""))
        {
            return;
        }

        if(statement.contains("let"))
        {
            current.setLeft(new Node("let", current));

            startIndex = statement.indexOf("let")+"let".length();

            if(statement.contains("if")){
                endIndex = statement.indexOf("if");
            } else if(statement.contains("then")){
                endIndex = statement.indexOf("then");
            } else {
                endIndex = statement.length();
            }

            nextStatement = statement.substring(startIndex, endIndex);

            current.getLeft().setCenter(new Node(nextStatement, current.getLeft()));
            recurse(current.getLeft().getCenter());
        }


        if(statement.contains("if"))
        {
            current.setCenter(new Node("if", current));

            startIndex = statement.indexOf("if")+"if".length();
            endIndex = (statement.contains("then") ? statement.indexOf("then") : statement.length());
            nextStatement = statement.substring(startIndex, endIndex);

            current.getCenter().setCenter(new Node(nextStatement, current.getCenter()));
            recurse(current.getCenter().getCenter());
        }


        if(current.getStatement().contains("then"))
        {
            current.setRight(new Node("then", current));

            startIndex = statement.indexOf("then")+"then".length();
            nextStatement = statement.substring(startIndex);

            current.getRight().setCenter(new Node(nextStatement, current.getRight()));
            recurse(current.getRight().getCenter());
        }
    }

    public List<String> retrieveStatements(final Node parsedTree)
    {
        return populateStatementList(parsedTree, new ArrayList<>());
    }

    private ArrayList<String> populateStatementList(final Node node, final ArrayList<String> statementList)
    {
        if(node == null)
        {
            return statementList;
        }

        final String statement = node.getStatement().trim();
        if(!(statement.contains("let") || statement.contains("if") || statement.contains("then")))
        {
            statementList.add(statement);
        }

        populateStatementList(node.getLeft(), statementList);
        populateStatementList(node.getCenter(), statementList);
        populateStatementList(node.getRight(), statementList);

        return statementList;
    }
}
