import java.util.*;

public class Knight extends Piece {
    private final Color color;

    public Knight(Color c) { this.color = c; }

    public String toString() {
	    if (this.color == Color.BLACK) { return "bn"; } 
        return "wn";
    }

    public List<String> moves(Board b, String loc) {
	    List<String> moves = new ArrayList<>();
        char col = loc.charAt(0);
        char row = loc.charAt(1);

        // Up 2; Left 1
        String currLoc = "" + (char)(col - 1) + (char)(row + 2);
        checkValidMove(b, currLoc, moves);
        
        // Up 2; Right 1
        currLoc = "" + (char)(col + 1) + (char)(row + 2);
        checkValidMove(b, currLoc, moves);

        // Up 1; Left 2
        currLoc = "" + (char)(col - 2) + (char)(row + 1);
        checkValidMove(b, currLoc, moves);

        // Up 1; Right 2
        currLoc = "" + (char)(col + 2) + (char)(row + 1);
        checkValidMove(b, currLoc, moves);

        // Down 1; Left 2
        currLoc = "" + (char)(col - 2) + (char)(row - 1);
        checkValidMove(b, currLoc, moves);

        // Down 1; Right 2
        currLoc = "" + (char)(col + 2) + (char)(row - 1);
        checkValidMove(b, currLoc, moves);

        // Down 2; Left 1
        currLoc = "" + (char)(col - 1) + (char)(row - 2);
        checkValidMove(b, currLoc, moves);

        // Down 2; Right 1
        currLoc = "" + (char)(col + 1) + (char)(row - 2);
        checkValidMove(b, currLoc, moves);
        
        return moves;
    }

    private void checkValidMove(Board b, String loc, List<String> moves) {
        if (stringInRange(loc)) {
            if (b.getPiece(loc) == null) {
                moves.add(loc);
            } else if (b.getPiece(loc).color() != 
                       this.color) {
                        moves.add(loc);
            }
        }
    }
}