package modele;

public class IceBloc extends Bloc {

    public IceBloc(Jeu _jeu, Case c) {
        super(_jeu, c);
    }

    @Override
    public boolean pousser(Direction d) {
        boolean moved = super.pousser(d);
        if (moved) {
            Case nextCase = jeu.getCaseInDirection(this.getCase(), d);
            if (nextCase instanceof Fire) {
                ((Fire) nextCase).extinguish();
            }
        }
        return moved;
    }
}