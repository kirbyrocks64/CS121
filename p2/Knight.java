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

        String currLoc = "" + (char)(col - 1) + (char)(row + 2);
        checkValidMove(currLoc, moves);
        
        currLoc = "" + (char)(col + 1) + (char)(row + 2);
        checkValidMove(currLoc, moves);

        currLoc = "" + (char)(col - 2) + (char)(row + 1);
        checkValidMove(currLoc, moves);

        currLoc = "" + (char)(col + 2) + (char)(row + 1);
        checkValidMove(currLoc, moves);

        currLoc = "" + (char)(col - 2) + (char)(row - 1);
        checkValidMove(currLoc, moves);

        currLoc = "" + (char)(col + 2) + (char)(row - 1);
        checkValidMove(currLoc, moves);

        currLoc = "" + (char)(col - 1) + (char)(row - 2);
        checkValidMove(currLoc, moves);

        currLoc = "" + (char)(col + 2) + (char)(row - 2);
        checkValidMove(currLoc, moves);

        return moves;
    }

    private void checkValidMove(String loc, List<String> moves) {
        if (stringInRange(loc)) {
            if (Board.theBoard().getPiece(loc) == null) {
                moves.add(loc);
            } else if (Board.theBoard().getPiece(loc).color() != 
                       this.color) {
                        moves.add(loc);
            }
        }
    }
}