import java.util.*;

public class Queen extends Piece {
    private final Color color;

    public Queen(Color c) { this.color = c; }

    public String toString() {
	    if (this.color == Color.BLACK) { return "bq"; } 
        return "wq";
    }

    public List<String> moves(Board b, String loc) {
        List<String> moves = new ArrayList<>();
        char col = loc.charAt(0);
        if (!charInRange(col)) { return moves; }
        char row = loc.charAt(1);
        if (!charInRange(row)) { return moves;}

        String currLoc;

        /* --------------- Checks straights --------------- */
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


        /* --------------- Checks diagonals --------------- */
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