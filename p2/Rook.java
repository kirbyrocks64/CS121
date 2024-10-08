import java.util.*;

public class Rook extends Piece {
    private final Color color;

    public Rook(Color c) { this.color = c; }

    public String toString() {
	    if (this.color == Color.BLACK) { return "br"; } 
        return "wr";
    }

    public List<String> moves(Board b, String loc) {
	throw new UnsupportedOperationException();
    }

}