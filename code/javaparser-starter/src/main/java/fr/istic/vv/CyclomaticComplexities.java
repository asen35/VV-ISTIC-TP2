package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.ArrayList;
import java.util.List;

public class CyclomaticComplexities extends VoidVisitorWithDefaults<Void> {

    private List<String> resultList = new ArrayList<>();
    private int cyclomaticComplexity;
    private List<String> ccNames = new ArrayList<>();
    private List<Integer> ccValues = new ArrayList<>();

    public List<String> getResultsList() {
        return resultList;
    }

    public List<String> getCcNames() {
        return new ArrayList<>(ccNames);
    }

    public List<Integer> getCcValues() {
        return new ArrayList<>(ccValues);
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        resultList.add(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        ccNames.add(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        ccValues.add(-1);

        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        cyclomaticComplexity = 1;

        for (Statement stmt : declaration.getBody().orElse(new BlockStmt()).getStatements()) {
            stmt.accept(this, arg);
        }

        resultList.add("Method: " + declaration.getName() + "; Parameter types : " + getParameterTypes(declaration) + "; Cyclomatic Complexity: " + cyclomaticComplexity);
        System.out.println("Method: " + declaration.getName() + "; Cyclomatic Complexity: " + cyclomaticComplexity);
        ccNames.add(String.valueOf(declaration.getName()));
        ccValues.add(cyclomaticComplexity);
    }

    private String getParameterTypes(MethodDeclaration methodDeclaration) {
        NodeList<Parameter> parameters = methodDeclaration.getParameters();
        List<String> parameterTypes = new ArrayList<>();

        for (Parameter parameter : parameters) {
            Type type = parameter.getType();
            parameterTypes.add(type.toString());
        }

        return String.join(", ", parameterTypes);
    }

    @Override
    public void visit(ForStmt forStmt, Void arg) {
        cyclomaticComplexity += 1;
        if (forStmt.getCompare().isPresent())
            cyclomaticComplexity += countBooleanConditions(forStmt.getCompare().get(), 0);
    }

    @Override
    public void visit(SwitchStmt switchStmt, Void arg) {
        NodeList<SwitchEntry> switchEntries = switchStmt.getEntries();
        int numberOfCases = 0;

        for (SwitchEntry switchEntry : switchEntries) {
            numberOfCases += switchEntry.getLabels().size();
        }

        cyclomaticComplexity += numberOfCases;
    }

    @Override
    public void visit(IfStmt ifStmt, Void arg) {
        cyclomaticComplexity++;
        cyclomaticComplexity += countBooleanConditions(ifStmt.getCondition(), 0);
    }

    @Override
    public void visit(WhileStmt whileStmt, Void arg) {
        cyclomaticComplexity++;
        cyclomaticComplexity += countBooleanConditions(whileStmt.getCondition(), 0);
        ;
    }

    @Override
    public void visit(DoStmt doStmt, Void arg) {
        cyclomaticComplexity++;
        cyclomaticComplexity += countBooleanConditions(doStmt.getCondition(), 0);
        ;
    }

    private int countBooleanConditions(Expression expr, int conditionCount) {
        if (expr instanceof BinaryExpr) {
            BinaryExpr binaryExpr = (BinaryExpr) expr;
            if (binaryExpr.getOperator() == BinaryExpr.Operator.AND
                    || binaryExpr.getOperator() == BinaryExpr.Operator.OR) {
                conditionCount++;
            }
            conditionCount = countBooleanConditions(binaryExpr.getLeft(), conditionCount);
            conditionCount = countBooleanConditions(binaryExpr.getRight(), conditionCount);
        }

        return conditionCount;
    }
}