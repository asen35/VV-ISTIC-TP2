# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce 
the same value for a given Java class. Build an example of such as class and include the code below or find one 
example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC
for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

Si la même valeur est produite, soit la classe n'a pas d'attributs ou 
alors lorsque toutes les paires de méthodes accèdent au même ensemble d'attributs. 


Dans cet exemple ci-dessous, le LCC et TCC auront la même valeur, voici le calcul : 

N = 3
NDC = 3
NIC = 0

NP = N * (N — 1)/2 => 3

TCC = NDC / NP => 1
LCC = (NDC + NIC)/NP => 1

```
public class TCCandLCC {

    private int x;
    private int y;

    public TCCandLCC(int x, int y){
        this.x = x;
        this.y = y;
    }


    public int mult() {
        return x*y;
    }

    public int sum() {
        return x+y;
    }

    public int sub() {
        return x-y;
    }
}
```

Non, le LCC ne peut pas être inférieur au TCC.