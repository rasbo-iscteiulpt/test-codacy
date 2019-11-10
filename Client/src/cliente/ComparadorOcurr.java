package cliente;

import java.util.Comparator;

import common.Resposta;

public class ComparadorOcurr implements Comparator<Resposta> {
	
    @Override
    public int compare(Resposta r1, Resposta r2) {
        return r2.getnVezes() - r1.getnVezes();
    }
}
