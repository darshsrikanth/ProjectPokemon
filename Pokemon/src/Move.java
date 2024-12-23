public class Move {
    String moveName;
    String moveType;
    int pp;
    int dmg;
    double acc;
    int priority;
    int burn;
    int par;
    int frz;
    int slp;

    public Move(String name, String type, int powerPoints, int damage, double accuracy, int pkPriority, int pkBurn, int pkPar, int pkFrz, int pkSlp) {
        moveName = name;
        moveType = type;
        pp = powerPoints;
        dmg = damage;
        acc = accuracy;
        priority = pkPriority;
        burn = pkBurn;
        par = pkPar;
        frz = pkFrz;
        slp = pkSlp;
    }

    @Override
    public String toString() {
        return "Move [moveName=" + moveName + ", moveType=" + moveType + ", pp=" + pp + ", dmg=" + dmg + ", acc=" + acc
                + ", priority=" + priority + ", burn=" + burn + ", par=" + par + ", frz=" + frz + ", slp=" + slp + "]";
    }
}



