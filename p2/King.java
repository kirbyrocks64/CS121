import java.util.*;

public class King extends Piece {
    private final Color color;

    public King(Color c) { this.color = c; }

    public String toString() {
	    if (this.color == Color.BLACK) { return "bk"; } 
        return "wk";
    }

    public List<String> moves(Board b, String loc) {
	throw new UnsupportedOperationException();
    }

}