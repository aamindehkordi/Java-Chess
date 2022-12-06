package tests.com.chess.tests;

import com.chess.model.pgn.PGNUtilities;
import com.chess.model.pgn.ParsePGNException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class TestPGNParser {
    //TODO: parser does not work :)


    @Test
    public void test1() throws IOException, ParsePGNException {
        doTest("/tests/com/chess/tests/pgn/t1.pgn");
    }

    @Test
    public void test2() throws IOException, ParsePGNException {
        doTest("/tests/com/chess/tests/pgn/t2.pgn");
    }

    @Test
    public void test3() throws IOException, ParsePGNException {
        doTest("/tests/com/chess/tests/pgn/t3.pgn");
    }

    @Test
    public void test4() throws IOException, ParsePGNException {
        doTest("/tests/com/chess/tests/pgn/t4.pgn");
    }

    @Test
    public void test5() throws IOException, ParsePGNException {
        doTest("/tests/com/chess/tests/pgn/smallerTest.pgn");
    }

    @Test
    public void test6() throws IOException, ParsePGNException {
        doTest("/tests/com/chess/tests/pgn/t6.pgn");
    }

    @Test
    public void test8() throws IOException, ParsePGNException {
        doTest("/tests/com/chess/tests/pgn/t8.pgn");
    }

    @Test
    public void test9() throws IOException, ParsePGNException {
        doTest("/tests/com/chess/tests/pgn/t9.pgn");
    }

    @Test
    public void testPawnPromotion() throws IOException, ParsePGNException {
        doTest("/tests/com/chess/tests/pgn/queenPromotion.pgn");
    }

    @Test
    public void test10() throws IOException, ParsePGNException {
        doTest("/tests/com/chess/tests/pgn/t10.pgn");
    }

    /*
    @Test
    public void test11() throws IOException, ParsePGNException {
        doTest("com/chess/tests/pgn/bigTest.pgn");
    }

    @Test
    public void test12() throws IOException, ParsePGNException {
        doTest("com/chess/tests/pgn/twic1047.pgn");
    }

    @Test
    public void test13() throws IOException, ParsePGNException {
        doTest("com/chess/tests/pgn/twic1046.pgn");
    }

    @Test
    public void test14() throws IOException, ParsePGNException {
        doTest("com/chess/tests/pgn/combined.pgn");
    }

    @Test
    public void test15() throws IOException, ParsePGNException {
        doTest("com/chess/tests/pgn/c2012.pgn");
    }

     */
    @Test
    public void testParens() throws ParsePGNException {

        final String gameText = "(+)-(-) (+)-(-) 1. e4 e6";
        final List<String> moves = PGNUtilities.processMoveText(gameText);
        assert(moves.size() == 2);

    }

    private static void doTest(final String testFilePath) throws IOException, ParsePGNException {
        //Convert the file path to a URL
        final URL url = TestPGNParser.class.getResource(testFilePath);

        //Get the file
        final File file = new File(Objects.requireNonNull(url).getFile());

        //Parse the file
        final String gameText = PGNUtilities.readPGNFile(file);

        //Get the moves
        final List<String> moves = PGNUtilities.processMoveText(gameText);

        //Print the moves
        System.out.println("moves = " +moves);
    }

}
