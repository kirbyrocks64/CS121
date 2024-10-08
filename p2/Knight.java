import java.util.*;

public class Knight extends Piece {
    private final Color color;

    public Knight(Color c) { this.color = c; }

    public String toString() {
	    if (this.color == Color.BLACK) { return "bn"; } 
        return "wn";
    }

    public List<String> moves(Board b, String loc) {
	throw new UnsupportedOperationException();
    }

}