public class Test {

    // Run "java -ea Test" to run with assertions enabled (If you run
    // with assertions disabled, the default, then assert statements
    // will not execute!)

    public static void test_pawn_create() {
        Board b = Board.theBoard();
        Piece.registerPiece(new PawnFactory());
        Piece p = Piece.createPiece("bp");
        b.addPiece(p, "a3");
        assert b.getPiece("a3") == p;
    }

    public static void test_misc() {
        char i = '5' + 1;
        System.out.println(i);
    }
    
    public static void main(String[] args) {
        test_pawn_create();
    }

}