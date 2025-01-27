import java.util.*;

public class King extends Piece {
    private final Color color;

    public King(Color c) { this.color = c; }

    public String toString() {
	    if (this.color == Color.BLACK) { return "bk"; } 
        return "wk";
    }

    public List<String> moves(Board b, String loc) {
	    List<String> moves = new ArrayList<>();
        char col = loc.charAt(0);
        if (!charInRange(col)) { return moves; }
        char row = loc.charAt(1);
        if (!charInRange(row)) { return moves;}

        // Move north
        String currLoc = "" + col + (char)(row + 1);
        checkValidMove(currLoc, moves);

        // Move northeast
        currLoc = "" + (char)(col + 1) + (char)(row + 1);
        checkValidMove(currLoc, moves);

        // Move east
        currLoc = "" + (char)(col + 1) + row;
        checkValidMove(currLoc, moves);

        // Move southeast
        currLoc = "" + (char)(col + 1) + (char)(row - 1);
        checkValidMove(currLoc, moves);

        // Move south
        currLoc = "" + col + (char)(row - 1);
        checkValidMove(currLoc, moves);

        // Move southwest
        currLoc = "" + (char)(col - 1) + (char)(row - 1);
        checkValidMove(currLoc, moves);

        // Move west
        currLoc = "" + (char)(col - 1) + row;
        checkValidMove(currLoc, moves);

        // Move northwest
        currLoc = "" + (char)(col - 1) + (char)(row + 1);
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