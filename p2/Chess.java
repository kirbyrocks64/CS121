import java.io.*;

public class Chess {
    private static final char[] pieces = {'k', 'q', 'n', 'b', 'r', 'p'};
    private static final char[] cols = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

    public static void main(String[] args) {
	if (args.length != 2) {
	    System.out.println("Usage: java Chess layout moves");
	}
	Piece.registerPiece(new KingFactory());
	Piece.registerPiece(new QueenFactory());
	Piece.registerPiece(new KnightFactory());
	Piece.registerPiece(new BishopFactory());
	Piece.registerPiece(new RookFactory());
	Piece.registerPiece(new PawnFactory());
	Board.theBoard().registerListener(new Logger());

	// args[0] is the layout file name
	// args[1] is the moves file name
	// Put your code to read the layout file and moves files
	// here.

    /* ------------ Reads layout file ------------ */
    File layoutFile = new File(args[0]);
    
    try {
        BufferedReader layoutReader = new BufferedReader(new FileReader(layoutFile));

        String layoutLine;
        while ((layoutLine = layoutReader.readLine()) != null) {
            if (layoutLine.charAt(0) == '#') {
                continue;
            }
            if (layoutLine.length() != 5) {
                throw new IllegalArgumentException("Layouts should have length 5");
            }

            char col = layoutLine.charAt(0);
            char row = layoutLine.charAt(1);
            char color = layoutLine.charAt(3);
            char symbol = layoutLine.charAt(4);

            if (col < 'a' || col > 'h') {
                throw new IllegalArgumentException("");
            } else if (row < '1' || row > '8') {
                throw new IllegalArgumentException();
            } else if (layoutLine.charAt(2) != '=') {
                throw new IllegalArgumentException();
            } else if (color != 'b' && color != 'w') {
                throw new IllegalArgumentException();
            } else {
                boolean truePiece = false;
                for (char c : pieces) {
                    if (c == symbol) {
                        truePiece = true;
                    }
                }
                if (!truePiece) {
                    throw new IllegalArgumentException();
                }
            }
            
            Piece newPiece = Piece.createPiece("" + color + symbol);
            String loc = "" + col + row;
            if (Board.theBoard().getPiece(loc) != null) {
                throw new IllegalArgumentException();
            }
            Board.theBoard().addPiece(newPiece, loc);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    /* ------------ Reads moves file ------------ */
    File movesFile = new File(args[1]);
    if (!movesFile.exists()) {
        throw new IllegalArgumentException();
    }

    try (BufferedReader movesReader = 
        new BufferedReader(new FileReader(movesFile))) {
        
        String movesLine;
        while ((movesLine = movesReader.readLine()) != null) {
            if (movesLine.charAt(0) == '#') {
                continue;
            }
            if (movesLine.length() != 5) {
                throw new IllegalArgumentException();
            }

            char startCol = movesLine.charAt(0);
            char startRow = movesLine.charAt(1);
            char endCol = movesLine.charAt(3);
            char endRow = movesLine.charAt(4);

            if (startCol < 'a' || startCol > 'h' || 
                endCol < 'a' || endCol > 'h') {
                throw new IllegalArgumentException();
            } else if (startRow < '1' || startRow > '8' || 
                       endRow < '1' || endRow > '8') {
                throw new IllegalArgumentException();
            } else if (movesLine.charAt(2) != '-') {
                throw new IllegalArgumentException();
            }

            String startLoc = "" + startCol + startRow;
            String endLoc = "" + endCol + endRow;
            Board.theBoard().movePiece(startLoc, endLoc);
        }
        
    } catch (IOException e) {
        e.printStackTrace();
    }

	// Leave the following code at the end of the simulation:
	System.out.println("Final board:");
	Board.theBoard().iterate(new BoardPrinter());
    }
}