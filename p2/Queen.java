import java.util.*;

public class Queen extends Piece {
    private final Color color;

    public Queen(Color c) { this.color = c; }

    public String toString() {
	    if (this.color == Color.BLACK) { return "bq"; } 
        return "wq";
    }

    public List<String> moves(Board b, String loc) {
	throw new UnsupportedOperationException();
    }

}