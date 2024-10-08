import java.util.*;

public class Bishop extends Piece {
    private final Color color;
    private static char[] cols = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private static char[] rows = {'8', '7', '6', '5', '4', '3', '2', '1'};

    public Bishop(Color c) { this.color = c; }

    public String toString() {
	    if (this.color == Color.BLACK) { return "bb"; } 
        return "wb";
    }

    public List<String> moves(Board b, String loc) {
        List<String> moves = new ArrayList<>();
        char col = loc.charAt(0);
        int numCol;
        for (int i = 0; i < cols.length; i++) {
            if (cols[i] == col) { numCol = i; }
        }

        char row = loc.charAt(1);
        int numRow;
        for (int i = 0; i < rows.length; i++) {
            if (rows[i] == row) { numRow = i; }
        }

        for (char i = row; i >= 'a'; i--) {

        }

        return moves;
    }

}