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
        int wordBeginsHere;

        if(current.getStatement().contains("let"))
        {
            current.setLeft(new Node("let", current));

            if(current.getStatement().contains("if")){
                wordBeginsHere = current.getStatement().indexOf("if");
            } else if(current.getStatement().contains("then")){
                wordBeginsHere = current.getStatement().indexOf("then");
            } else {
                wordBeginsHere = current.getStatement().length();
            }

            current.getLeft().setCenter(new Node(current.getStatement().substring(current.getStatement().indexOf("let")+"let".length(),
                    wordBeginsHere),
                    current.getLeft()));
            recurse(current.getLeft().getCenter());
        }


        if(current.getStatement().contains("if"))
        {
            current.setCenter(new Node("if", current));
            wordBeginsHere = (current.getStatement().contains("then") ? current.getStatement().indexOf("then") : current.getStatement().length());

            current.getCenter().setCenter(new Node(current.getStatement().substring(current.getStatement().indexOf("if")+"if".length(),
                    wordBeginsHere),
                    current.getCenter()));
            recurse(current.getCenter().getCenter());
        }


        if(current.getStatement().contains("then"))
        {
            current.setRight(new Node("then", current));
            current.getRight().setCenter(new Node(current.getStatement().substring(current.getStatement().indexOf("then")+"then".length()),
                    current.getRight()));
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
