public class Board {

    private Piece[][] pieces = new Piece[8][8];
    private static char[] cols = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private static char[] rows = {'8', '7', '6', '5', '4', '3', '2', '1'};

    private static Board board;

    private Board() { }
    
    public static Board theBoard() {
        if (board == null) {
            return new Board();
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
        if (!goodMove) {
            throw new IllegalArgumentException
                ("Cannot movePiece; move is illegal");
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
	throw new UnsupportedOperationException();
    }

    public void removeListener(BoardListener bl) {
	throw new UnsupportedOperationException();
    }

    public void removeAllListeners() {
	throw new UnsupportedOperationException();
    }

    public void iterate(BoardInternalIterator bi) {
	throw new UnsupportedOperationException();
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