public class Pokemon {
    String name;
    String type;
    int hp;
    int maxHp;
    Move[] moves;
    String sprite;
    int speed;
    boolean isBurned = false;
    boolean isParalyzed = false;
    boolean isFrozen = false;
    boolean isAsleep = false;
    int sleepTurns = 0;
    int frozenTurns = 0;

    public Pokemon(String name, String type, int hp, int maxHp, Move[] moves, String sprite, int speed) {
        this.name = name;
        this.type = type;
        this.hp = hp;
        this.maxHp = maxHp;
        this.moves = moves;
        this.sprite = sprite;
        this.speed = speed;
    }
}
