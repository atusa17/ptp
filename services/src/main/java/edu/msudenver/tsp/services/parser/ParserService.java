package edu.msudenver.tsp.services.parser;

import edu.msudenver.tsp.services.DefinitionService;
import edu.msudenver.tsp.services.dto.Definition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
class ParserService {
    @Autowired
    DefinitionService definitionService;

    public boolean parseUserInput(final String userInput)
    {
        try {
            final Node tree = parseRawInput(userInput);
            final List<String> statements = retrieveStatements(tree);
            final Map<String, Definition> definitions = collectDefinitions(statements);
            return true;
        } catch(final Exception e) {
            LOG.error(e.getMessage());
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Node parseRawInput(String input)
    {
        input = input.toLowerCase();

        final Node root = new Node(input, null);

        if(input.equals(""))
        {
            return root;
        }

        recurse(root);

        return root;
    }

    public void recurse(final Node current)
    {
        final String statement;

        if (current != null) {
            statement = current.getStatement();
        } else {
            return;
        }

        if(statement.contains("let"))
        {
            separateByLet(current, statement);
        }

        if(statement.contains("if"))
        {
            separateByIf(current, statement);
        }


        if(current.getStatement().contains("then"))
        {
            separateByThen(current, statement);
        }
    }

    public void separateByLet(final Node current, final String statement){
        final int startIndex;
        final int endIndex;
        final String nextStatement;

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

    public void separateByIf(final Node current, final String statement){
        final int startIndex;
        final int endIndex;
        final String nextStatement;

        current.setCenter(new Node("if", current));

        startIndex = statement.indexOf("if")+"if".length();
        endIndex = (statement.contains("then") ? statement.indexOf("then") : statement.length());
        nextStatement = statement.substring(startIndex, endIndex);

        current.getCenter().setCenter(new Node(nextStatement, current.getCenter()));
        recurse(current.getCenter().getCenter());
    }

    public void separateByThen(final Node current, final String statement){
        final int startIndex;
        final String nextStatement;

        current.setRight(new Node("then", current));

        startIndex = statement.indexOf("then")+"then".length();
        nextStatement = statement.substring(startIndex);

        current.getRight().setCenter(new Node(nextStatement, current.getRight()));
        recurse(current.getRight().getCenter());
    }

    public List<String> retrieveStatements(final Node parsedTree)
    {
        return populateStatementList(parsedTree, new ArrayList<>());
    }

    public List<String> populateStatementList(final Node node, final List<String> statementList)
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

    public Map<String, Definition> collectDefinitions(final List<String> statements) {
        String[] words;
        final Map<String, Definition> definitionMap = new HashMap<>();

        for(int i=0; i<statements.size(); i++) {
            words = statements.get(i).split(" ");

            for(final String w : words) {
                if(!definitionMap.containsKey(w)) {
                    final Optional<Definition> def = queryDefinitionService(w);
                    if(def.isPresent()){
                        definitionMap.put(w, def.get());
                    }
                }
            }
        }

        return definitionMap;
    }

    public Optional<Definition> queryDefinitionService(final String w) {
        return definitionService.findByName(w);
    }
}
