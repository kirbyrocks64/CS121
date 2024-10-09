import java.util.*;

public class Board {

    private Piece[][] pieces = new Piece[8][8];
    private static char[] cols = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private static char[] rows = {'8', '7', '6', '5', '4', '3', '2', '1'};
    private final List<BoardListener> listeners = new ArrayList<>();

    private static Board board;

    private Board() { }
    
    public static Board theBoard() {
        if (board == null) {
            board = new Board();
        }
	    return board;
    }

    // Returns piece at given loc or null if no such piece
    // exists
    public Piece getPiece(String loc) {
	    int col = convertCol(loc.charAt(0));
        int row = convertRow(loc.charAt(1));

        return pieces[row][col];
    }

    public void addPiece(Piece p, String loc) {
	    int col = convertCol(loc.charAt(0));
        int row = convertRow(loc.charAt(1));

        if (pieces[row][col] == null) {
            pieces[row][col] = p;
        } else {
            throw new IllegalArgumentException
                ("Cannot addPiece; space occupied");
        }
    }

    public void movePiece(String from, String to) {
	    int startCol = convertCol(from.charAt(0));
        int startRow = convertRow(from.charAt(1));
        int endCol = convertCol(to.charAt(0));
        int endRow = convertRow(to.charAt(1));

        Piece piece = getPiece(from);
        if (piece == null) {
            throw new IllegalArgumentException
                ("Cannot movePiece; no piece to move");
        }

        boolean goodMove = false;
        for (String move : piece.moves(board, from)) {
            if (move.equals(to)) {
                goodMove = true;
                
            }
        }
        System.out.println("Moving " + piece.toString() + " from " + from + " to " + to);
        if (!goodMove) {
            throw new IllegalArgumentException
                ("Cannot movePiece; move is illegal");
        }

        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onMove(from, to, piece);
        }

        if (Board.theBoard().getPiece(to) != null) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onCapture(piece, 
                                           Board.theBoard().getPiece(to));
            }
        }

        pieces[startRow][startCol] = null;
        pieces[endRow][endCol] = piece;
    }

    public void clear() {
	    for (Piece[] prow : pieces) {
            for (int j = 0; j < prow.length; j++) {
                prow[j] = null;
            }
        }
    }

    public void registerListener(BoardListener bl) {
	    listeners.add(bl);
    }

    public void removeListener(BoardListener bl) {
	    listeners.remove(bl);
    }

    public void removeAllListeners() {
	    listeners.clear();
    }

    public void iterate(BoardInternalIterator bi) {
	    for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String loc = "" + cols[j] + rows[i];
                Piece piece = Board.theBoard().getPiece(loc);
                bi.visit(loc, piece);
            }
        }
    }

    private int convertCol(char col) {
        for (int i = 0; i < cols.length; i++) {
            if (cols[i] == col) { return i; }
        }
        throw new IllegalArgumentException("Column out of range");
    }

    private int convertRow(char row) {
        for (int i = 0; i < rows.length; i++) {
            if (rows[i] == row) { return i; }
        }
        throw new IllegalArgumentException("Row out of range");
    }
}