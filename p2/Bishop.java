import java.util.*;

public class Bishop extends Piece {
    private final Color color;

    public Bishop(Color c) { this.color = c; }

    public String toString() {
	    if (this.color == Color.BLACK) { return "bb"; } 
        return "wb";
    }

    public List<String> moves(Board b, String loc) {
        List<String> moves = new ArrayList<>();
        char col = loc.charAt(0);
        char row = loc.charAt(1);

        String currLoc;
        char Wcols = (char)(col - 1);
        boolean NWblocked = false;
        char Ecols = (char)(col + 1);
        boolean NEblocked = false;
        for (char i = (char)(row + 1); i <= '8'; i++) {
            if (charInRange(Wcols) && !NWblocked) {
                currLoc = "" + Wcols + i;
                if (Board.theBoard().getPiece(currLoc) == null) {
                    moves.add(currLoc);
                } else {
                    NWblocked = true;
                    if (Board.theBoard().getPiece(currLoc).color() != 
                        this.color) {
                            moves.add(currLoc);
                    }
                }
                Wcols -= 1;
            }

            if(charInRange(Ecols) && !NEblocked) {
                currLoc = "" + Ecols + i;
                if (Board.theBoard().getPiece(currLoc) == null) {
                    moves.add(currLoc);
                } else {
                    NEblocked = true;
                    if (Board.theBoard().getPiece(currLoc).color() != 
                        this.color) {
                            moves.add(currLoc);
                    }
                }
                Ecols += 1;
            }
        }

        Wcols = (char)(col - 1);
        boolean SWblocked = false;
        Ecols = (char)(col + 1);
        boolean SEblocked = false;
        for (char i = (char)(row - 1); i >= '1'; i--) {
            if (charInRange(Wcols) && !SWblocked) {
                currLoc = "" + Wcols + i;
                if (Board.theBoard().getPiece(currLoc) == null) {
                    moves.add(currLoc);
                } else {
                    SWblocked = true;
                    if (Board.theBoard().getPiece(currLoc).color() != 
                        this.color) {
                            moves.add(currLoc);
                    }
                }
                Wcols -= 1;
            }

            if(charInRange(Ecols) && !SEblocked) {
                currLoc = "" + Ecols + i;
                if (Board.theBoard().getPiece(currLoc) == null) {
                    moves.add(currLoc);
                } else {
                    SEblocked = true;
                    if (Board.theBoard().getPiece(currLoc).color() != 
                        this.color) {
                            moves.add(currLoc);
                    }
                }
                Ecols += 1;
            }
        }

        return moves;
    }
}