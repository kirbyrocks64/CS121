import java.util.*;

public class Pawn extends Piece {
    private final Color color;

    public Pawn(Color c) { this.color = c; }

    public String toString() {
	    if (this.color == Color.BLACK) { return "bp"; } 
        return "wp";
    }

    public List<String> moves(Board b, String loc) {
	    List<String> moves = new ArrayList<>();
        char col = loc.charAt(0);
        char row = loc.charAt(1);

        String currLoc;
        if (this.color == Color.BLACK) {
            currLoc = "" + col + (char)(row - 1);
            if (stringInRange(currLoc) && 
                b.getPiece(currLoc) == null) { 
                    moves.add(currLoc); 
                    if (row == '7') {
                        currLoc = "" + col + (char)(row - 2);
                        if (b.getPiece(currLoc) == null) {
                            moves.add(currLoc); 
                        }
                    }
            }

            currLoc = "" + (char)(col - 1) + (char)(row - 1);
            if (stringInRange(currLoc) && b.getPiece(currLoc) != null && 
                b.getPiece(currLoc).color() == Color.WHITE) {
                    moves.add(currLoc);
            }

            currLoc = "" + (char)(col + 1) + (char)(row - 1);
            if (stringInRange(currLoc) && b.getPiece(currLoc) != null && 
                b.getPiece(currLoc).color() == Color.WHITE) {
                    moves.add(currLoc);
            }
        } else {
            currLoc = "" + col + (char)(row + 1);
            if (stringInRange(currLoc) && 
                b.getPiece(currLoc) == null) { 
                    moves.add(currLoc); 
                    if (row == '2') {
                        currLoc = "" + col + (char)(row + 2);
                        if (b.getPiece(currLoc) == null) {
                            moves.add(currLoc); 
                        }
                    }
            }

            currLoc = "" + (char)(col - 1) + (char)(row + 1);
            if (stringInRange(currLoc) && b.getPiece(currLoc) != null && 
                b.getPiece(currLoc).color() == Color.BLACK) {
                    moves.add(currLoc);
            }

            currLoc = "" + (char)(col + 1) + (char)(row + 1);
            if (stringInRange(currLoc) && b.getPiece(currLoc) != null && 
                b.getPiece(currLoc).color() == Color.BLACK) {
                    moves.add(currLoc);
            }
        }

        return moves;
    }
}