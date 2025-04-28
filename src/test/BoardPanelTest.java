package test;//package test;
//
//import java.lang.reflect.Constructor;
//import org.junit.*;
//import org.psnbtech.BoardPanel;
//import org.psnbtech.Tetris;
//import org.psnbtech.TileType;
//
//import static org.junit.Assert.*;
//
//public class BoardPanelTest {
//    private BoardPanel board;
//    private Tetris realGame;
//
//    @Before
//    public void setUp() throws Exception {
//        // 1) grab the private no-arg constructor
//        Constructor<Tetris> ctor = Tetris.class.getDeclaredConstructor();
//        ctor.setAccessible(true);
//
//        // 2) invoke it
//        realGame = ctor.newInstance();
//
//        // 3) now you can pass it into your BoardPanel
//        board = new BoardPanel(realGame);
//    }
//
//    @Test
//    public void testClear() {
//        board.clear();
//
//        boolean result = board.isValidAndEmpty(TileType.TypeL, 2, 2, 0);
//        assertTrue(result);
//    }
//
//    @Test
//    public void testAddPiece() {
//        board.clear();
//
//        board.addPiece(TileType.TypeL, 2, 2, 0);
//
//        boolean result = board.isValidAndEmpty(TileType.TypeL, 2, 2, 0);
//        assertFalse(result);
//    }
//

//}


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.psnbtech.BoardPanel;
import org.psnbtech.Tetris;
import org.psnbtech.TileType;

import java.awt.image.BufferedImage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

// The Tetris class' methods are private so we need another way of accessing them
@RunWith(MockitoJUnitRunner.class)
public class BoardPanelTest {

    // Create a Mockito mock for the Tetris dependency
    @Mock
    private Tetris fakeGame;

    // Have Mockito call the BoardPanel(Tetris) constructor for us
    @InjectMocks
    private BoardPanel board;

    @Before
    public void setUp() {
        // Stub out any methods paintComponent() or our logic tests will hit
        when(fakeGame.getPieceType()).thenReturn(TileType.TypeT);
        when(fakeGame.getPieceCol()).thenReturn(3);
        when(fakeGame.getPieceRow()).thenReturn(0);
        when(fakeGame.getPieceRotation()).thenReturn(1);
    }

    @Test
    public void testCheckLinesAllEmpty() {
        board.clear();

        int result = board.checkLines();
        assertEquals(22, result);
    }

    @Test
    public void testCheckLinesWithFullRow() {
        board.clear();

        board.addPiece(TileType.TypeO, 0, 2, 0);
        board.addPiece(TileType.TypeO, 2, 2, 0);
        board.addPiece(TileType.TypeO, 4, 2, 0);
        board.addPiece(TileType.TypeO, 6, 2, 0);
        board.addPiece(TileType.TypeO, 8, 2, 0);

        int result = board.checkLines();
        assertEquals(22, result);
    }

    @Test
    public void testClearLeavesBoardEmpty() {
        board.clear();

        assertTrue(board.isValidAndEmpty(TileType.TypeL, 2, 2, 0));
    }

    @Test
    public void testAddPieceMakesOccupied() {
        board.clear();

        board.addPiece(TileType.TypeL, 2, 2, 0);

        assertFalse(board.isValidAndEmpty(TileType.TypeL, 2, 2, 0));
    }

    @Test
    public void testNegativeColumnIsNotValid() {
        board.clear();

        assertFalse(board.isValidAndEmpty(TileType.TypeL, -2, 2, 0));
    }

    @Test
    public void testNegativeRowIsNotValid() {
        board.clear();

        assertFalse(board.isValidAndEmpty(TileType.TypeL, 2, -2, 0));
    }

    @Test
    public void testPaintComponentDoesNotThrowWithGamePaused() {
        when(fakeGame.isPaused()).thenReturn(true);

        // Create an off-screen image, so the board has *something* to draw onto
        BufferedImage img = new BufferedImage(
                BoardPanel.PANEL_WIDTH, BoardPanel.PANEL_HEIGHT,
                BufferedImage.TYPE_INT_ARGB
        );
        board.paintComponent(img.getGraphics());
        // no exception = success
    }

    @Test
    public void testPaintComponentDoesNotThrowWithNewGame() {
        when(fakeGame.isPaused()).thenReturn(false);
        when(fakeGame.isNewGame()).thenReturn(true);

        // Create an off-screen image, so the board has *something* to draw onto
        BufferedImage img = new BufferedImage(
                BoardPanel.PANEL_WIDTH, BoardPanel.PANEL_HEIGHT,
                BufferedImage.TYPE_INT_ARGB
        );
        board.paintComponent(img.getGraphics());
        // no exception = success
    }

    @Test
    public void testPaintComponentDoesNotThrowWithGameOver() {
        when(fakeGame.isPaused()).thenReturn(false);
        when(fakeGame.isNewGame()).thenReturn(false);
        when(fakeGame.isGameOver()).thenReturn(true);

        // Create an off-screen image, so the board has *something* to draw onto
        BufferedImage img = new BufferedImage(
                BoardPanel.PANEL_WIDTH, BoardPanel.PANEL_HEIGHT,
                BufferedImage.TYPE_INT_ARGB
        );
        board.paintComponent(img.getGraphics());
        // no exception = success
    }

    @Test
    public void testPaintComponentDoesNotThrow() {
        // Create an off-screen image, so the board has *something* to draw onto
        BufferedImage img = new BufferedImage(
                BoardPanel.PANEL_WIDTH, BoardPanel.PANEL_HEIGHT,
                BufferedImage.TYPE_INT_ARGB
        );
        board.paintComponent(img.getGraphics());
        // no exception = success
    }
}
