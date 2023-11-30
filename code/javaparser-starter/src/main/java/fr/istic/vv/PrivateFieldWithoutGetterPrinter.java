package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithMembers;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PrivateFieldWithoutGetterPrinter extends VoidVisitorWithDefaults<Void> {

    private List<String> resultList = new ArrayList<>();

    public List<String> getResultsList() {
        return resultList;
    }

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        resultList.add(declaration.getFullyQualifiedName().orElse("[Anonymous]"));
        System.out.println(declaration.getFullyQualifiedName().orElse("[Anonymous]"));

        for (FieldDeclaration field : declaration.getFields()) {
            if (field.isPrivate()) {
                field.accept(this, arg);
            }
        }

        for (MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Printing nested types in the top level
        for (BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof TypeDeclaration)
                member.accept(this, arg);
        }


    }


    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        if (!declaration.isPublic()) return;
    }

    @Override
    public void visit(FieldDeclaration declaration, Void arg) {
        if (declaration.isPrivate()) {
            String fieldName = declaration.getVariables().get(0).getNameAsString();

            if (!hasGetter((NodeWithMembers<?>) declaration.getParentNode().get(), fieldName)) {
                resultList.add("    No getter found for field: " + fieldName);
                System.out.println("    No getter found for field: " + fieldName);
            }
        }
    }

    private boolean hasGetter(NodeWithMembers<?> classOrInterface, String fieldName) {
        for (BodyDeclaration<?> member : classOrInterface.getMembers()) {
            if (member instanceof MethodDeclaration) {
                MethodDeclaration method = (MethodDeclaration) member;
                String methodName = method.getNameAsString();
                if (method.isPublic() && method.getNameAsString().equalsIgnoreCase("get" + fieldName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
