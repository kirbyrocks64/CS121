import java.util.*;

public class Rook extends Piece {
    private final Color color;

    public Rook(Color c) { this.color = c; }

    public String toString() {
	    if (this.color == Color.BLACK) { return "br"; } 
        return "wr";
    }

    public List<String> moves(Board b, String loc) {
        List<String> moves = new ArrayList<>();
        char col = loc.charAt(0);
        if (!charInRange(col)) { return moves; }
        char row = loc.charAt(1);
        if (!charInRange(row)) { return moves;}

        String currLoc;
        for (char i = (char)(row + 1); i <= '8'; i++) {
            currLoc = "" + col + i;
            if (Board.theBoard().getPiece(currLoc) == null) {
                moves.add(currLoc);
            } else if (Board.theBoard().getPiece(currLoc).color() != 
                        this.color) {
                        moves.add(currLoc);
                        break;
            }
        }

        for (char i = (char)(row - 1); i >= '1'; i--) {
            currLoc = "" + col + i;
            if (Board.theBoard().getPiece(currLoc) == null) {
                moves.add(currLoc);
            } else if (Board.theBoard().getPiece(currLoc).color() != 
                        this.color) {
                        moves.add(currLoc);
                        break;
            }
        }

        for (char i = (char)(col + 1); i <= 'h'; i++) {
            currLoc = "" + i + row;
            if (Board.theBoard().getPiece(currLoc) == null) {
                moves.add(currLoc);
            } else if (Board.theBoard().getPiece(currLoc).color() != 
                        this.color) {
                        moves.add(currLoc);
                        break;
            }
        }

        for (char i = (char)(col - 1); i >= 'a'; i--) {
            currLoc = "" + i + row;
            if (Board.theBoard().getPiece(currLoc) == null) {
                moves.add(currLoc);
            } else if (Board.theBoard().getPiece(currLoc).color() != 
                        this.color) {
                        moves.add(currLoc);
                        break;
            }
        }

        return moves;
    }
}