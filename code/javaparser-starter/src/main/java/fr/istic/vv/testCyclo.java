package fr.istic.vv;

public class testCyclo {
    private void doIt() {
        //+1

        //+1 => 2
        if (true) {

        } else {

        }

        //+2 => 4
        if (true && true) {

        } else {

        }

        //+1 => 5
        switch (5) {
            case 5:
                ;
        }

        //+2 => 7
        switch (5) {
            case 5:
                ;
            case 6:
                ;
        }

        //+1 => 8
        int i = 0;
        while (i < 2) {
            i++;
        }

        //+2 => 10
        while (i < 3 && i < 4) {
            i++;
        }

        //+1 => 11
        for (i = 0; i < 5; i++) {

        }

        //+2 => 13
        for (i = 0; i < 6 && i < 7; i++) {

        }
    }
}
