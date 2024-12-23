public class BattleSimulator {
    
    // Assuming TypeChecker's methods are made static and accessible
	//Code is mostly found here for type matchups in pokemon: https://codereview.stackexchange.com/questions/60965/pokemon-type-evaluator

	
    static Double normMulti1 = 1.0, flyMulti1 = 1.0, fightMulti1 = 1.0, fireMulti1 = 1.0,
             waterMulti1 = 1.0, elecMulti1 = 1.0, grassMulti1 = 1.0, bugMulti1 = 1.0,
             poisMulti1 = 1.0, darkMulti1 = 1.0, psyMulti1 = 1.0, ghostMulti1 = 1.0,
             groundMulti1 = 1.0, rockMulti1 = 1.0, steelMulti1 = 1.0, iceMulti1 = 1.0,
             dragMulti1 = 1.0, faeMulti1 = 1.0;
    
    static Double[] multiList = {normMulti1, flyMulti1, fightMulti1, fireMulti1, 
    		waterMulti1, elecMulti1, grassMulti1, bugMulti1, groundMulti1, rockMulti1,
    		steelMulti1, iceMulti1, dragMulti1, faeMulti1};
    
    static String[] elements = {"Normal", "Flying", "Fighting", "Fire", "Water",
 	        "Electric", "Grass", "Bug", "Poison", "Dark", "Psychic", "Ghost",
 	        "Ground", "Rock", "Steel", "Ice", "Dragon", "Fairy"};
    
    
    public static void initTypeChecker(String moveType) {
    	for (int i = 0; i < elements.length; i++) {
    	if (moveType == elements[i]) {
            switch(elements[i]) {
                case "Normal":
                    fightMulti1 = 2.0;
                    ghostMulti1 = 0.0;
                    break;
                case "Flying":
                    elecMulti1 = 2.0; iceMulti1 = 2.0; rockMulti1 = 2.0;
                    bugMulti1 = 0.5; fightMulti1 = 0.5; grassMulti1 = 0.5;
                    groundMulti1 = 0.0;
                    break;
                case "Fighting":
                    flyMulti1 = 2.0; psyMulti1 = 2.0;
                    bugMulti1 = 0.5; rockMulti1 = 0.5;
                    break;
                case "Fire":
                    groundMulti1 = 2.0; rockMulti1 = 2.0; waterMulti1 = 2.0;
                    bugMulti1 = 0.5; faeMulti1 = 0.5; fireMulti1 = 0.5;
                    grassMulti1 = 0.5; iceMulti1 = 0.5; steelMulti1 = 0.5;
                    break;
                case "Water":
                    elecMulti1 = 2.0; grassMulti1 = 2.0;
                    fireMulti1 = 0.5; iceMulti1 = 0.5;
                    steelMulti1 = 0.5; waterMulti1 = 0.5;
                    break;
                case "Electric":
                    groundMulti1 = 2.0;
                    elecMulti1 = 0.5; flyMulti1 = 0.5; steelMulti1 = 0.5;
                    break;
                case "Grass":
                    bugMulti1 = 2.0; iceMulti1 = 2.0;
                    fireMulti1 = 2.0; flyMulti1 = 2.0; poisMulti1 = 2.0;
                    elecMulti1 = 0.5; grassMulti1 = 0.5;
                    groundMulti1 = 0.5; waterMulti1 = 0.5;
                    break;
                case "Bug":
                    fireMulti1 = 2.0; flyMulti1 = 2.0; rockMulti1 = 2.0;
                    fightMulti1 = 0.5; grassMulti1 = 0.5; groundMulti1 = 0.5;
                    break;
                case "Poison":
                    groundMulti1 = 2.0; psyMulti1 = 2.0;
                    bugMulti1 = 0.5; faeMulti1 = 0.5;
                    fightMulti1 = 0.5; grassMulti1 = 0.5; poisMulti1 = 0.5;
                    break;
                case "Dark":
                    bugMulti1 = 2.0; faeMulti1 = 2.0; fightMulti1 = 2.0;
                    darkMulti1 = 0.5; ghostMulti1 = 0.5;
                    psyMulti1 = 0.0;
                    break;
                case "Psychic":
                    bugMulti1 = 2.0; darkMulti1 = 2.0; ghostMulti1 = 2.0;
                    fightMulti1 = 0.5; psyMulti1 = 0.5;
                    break;
                case "Ghost":
                    darkMulti1 = 2.0; ghostMulti1 = 2.0;
                    bugMulti1 = 0.5; poisMulti1 = 0.5;
                    fightMulti1 = 0.0; normMulti1 = 0.0;
                    break;
                case "Ground":
                    iceMulti1 = 2.0; grassMulti1 = 2.0; waterMulti1 = 2.0;
                    poisMulti1 = 0.5; rockMulti1 = 0.5;
                    elecMulti1 = 0.0;
                    break;
                case "Rock":
                    fightMulti1 = 2.0; grassMulti1 = 2.0;
                    groundMulti1 = 2.0; steelMulti1 = 2.0; waterMulti1 = 2.0;
                    fireMulti1 = 0.5; flyMulti1 = 0.5;
                    normMulti1 = 0.5; poisMulti1 = 0.5;
                    break;
                case "Steel":
                    fightMulti1 = 2.0; fireMulti1 = 2.0; groundMulti1 = 2.0;
                    bugMulti1 = 0.5; dragMulti1 = 0.5; faeMulti1 = 0.5;
                    flyMulti1 = 0.5; grassMulti1 = 0.5; iceMulti1 = 0.5;
                    normMulti1 = 0.5; psyMulti1 = 0.5;
                    rockMulti1 = 0.5; steelMulti1 = 0.5;
                    poisMulti1 = 0.0;
                    break;
                case "Ice":
                    fightMulti1 = 2.0; fireMulti1 = 2.0;
                    rockMulti1 = 2.0; steelMulti1 = 2.0;
                    iceMulti1 = 0.5;
                    break;
                case "Dragon":
                    dragMulti1 = 2.0; faeMulti1 = 2.0; iceMulti1 = 2.0;
                    elecMulti1 = 0.5; fireMulti1 = 0.5;
                    grassMulti1 = 0.5; waterMulti1 = 0.5;
                    break;
                case "Fairy":
                    poisMulti1 = 2.0; steelMulti1 = 2.0;
                    bugMulti1 = 0.5; darkMulti1 = 0.5; fightMulti1 = 0.5;
                    dragMulti1 = 0.0;
                    
                break; // final break purposely indented such
                
            }
    	}
    	}
    }

    public static double getEffectiveness(String moveType, String targetType) {
        // Initialize the type checker for Rock type
        initTypeChecker(targetType);
        Double[] multiList = {normMulti1, flyMulti1, fightMulti1, fireMulti1, 
        		waterMulti1, elecMulti1, grassMulti1, bugMulti1, groundMulti1, rockMulti1,
        		steelMulti1, iceMulti1, dragMulti1, faeMulti1};
        // Match the move type with the corresponding multiplier
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].equalsIgnoreCase(moveType)) {
                return multiList[i];
            }
        }
        return 1.0; // Default multiplier if no match found
    }

    public static void simulateBattle(Pokemon attacker, String targetType) {
        System.out.println("Simulating battle against " + targetType + " type Pokemon.");
        for (Move move : attacker.moves) {
            double effectiveness = getEffectiveness(move.moveType, targetType);
            double effectiveDamage = move.dmg * effectiveness;
            if (attacker.type == move.moveType)
            	effectiveDamage = effectiveDamage * 1.5;
            
            
            
            System.out.println(attacker.name + " uses " + move.moveName + " (" + move.moveType + 
                ") with effectiveness: " + effectiveness + ". Estimated damage: " + effectiveDamage);
        }
    }

    public static void main(String[] args) {
        // Example usage
    	  Move[] moves = {
    	            new Move("Tackle", "Normal", 35, 40, 1.0, 0, 0, 0, 0, 0),
    	            new Move("Rock Smash", "Fighting", 15, 75, 0.9, 0, 0, 0, 0, 0),
    	            new Move("Water Gun", "Water", 25, 40, 1.0, 0, 0, 0, 0, 0)
    	        };
    	        Pokemon squirtle = new Pokemon("Squirtle", "Water", 100, 100, moves, "SquirtleFrontSprite", 50);

        simulateBattle(squirtle, "Fire");
    }
}
