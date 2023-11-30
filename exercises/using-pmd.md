# Using PMD

Pick a Java project from Github (see the [instructions](../sujet.md) for suggestions). Run PMD on its source code using any ruleset. Describe below an issue found by PMD that you think should be solved (true positive) and include below the changes you would add to the source code. Describe below an issue found by PMD that is not worth solving (false positive). Explain why you would not solve this issue.

You can use the default [rule base](https://github.com/pmd/pmd/blob/master/pmd-java/src/main/resources/rulesets/java/quickstart.xml) available on the source repository of PMD.

## Answer

- Vrai positif :
  - Problème :
    - Règle: ClassWithOnlyPrivateConstructorsShouldBeFinal 
    - Emplacement: /mnt/c/Users/maxim/Downloads/commons-collections-master/src/main/java/org/apache/commons/collections4/CollectionUtils.java:57
    - Description: Cette classe n'a que des constructeurs privés et pourrait être définie final.
  - Changement :
    - Ajouter le modificateur final à la classe.
- Faux positif :
  - Problème :
    - Règle : UnnecessaryImport 
    - Emplacement : /mnt/c/Users/maxim/Downloads/commons-collections-master/src/main/java/org/apache/commons/collections4/functors/AllPredicate.java:19-21 
    - Description : Import statique inutilisé 'org.apache.commons.collections4.functors.FunctorUtils.coerce' et d'autres.
  - Pourquoi pas de changement ?
    - Les importations statiques peuvent faire partie d'un code plus large ou d'utilisations futures.