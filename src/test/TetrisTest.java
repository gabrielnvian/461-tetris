package test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.psnbtech.*;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

/**
 * Now we never call the real constructor, so no AWT repaint ever fires.
 */
@RunWith(MockitoJUnitRunner.class)
public class TetrisTest {

    @Mock
    private BoardPanel mockBoard;

    @Mock
    private SidePanel mockSide;

    private Tetris tetris;

    private Field random;
    private Field logicTimer;
    private Field nextType;

    private Method spawnPiece;
    private Method resetGame;
    private Method updateGame;
    private Method rotatePiece;

    @Before
    public void setUp() throws Exception {
        ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
        Constructor<Object> objConstructor = Object.class.getDeclaredConstructor();
        // Build a "synthetic" constructor
        Constructor<Tetris> fakeConstructor = (Constructor<Tetris>) rf.newConstructorForSerialization(Tetris.class, objConstructor);
        // Mark it as accessible, to bypass the private clauses
        fakeConstructor.setAccessible(true);

        // Instantiate without running Tetris.<init>()
        tetris = fakeConstructor.newInstance();

        // Inject our mocks before any logic runs
        Field board = Tetris.class.getDeclaredField("board");
        board.setAccessible(true);
        board.set(tetris, mockBoard);

        Field side = Tetris.class.getDeclaredField("side");
        side.setAccessible(true);
        side.set(tetris, mockSide);

        // Inject missing values
        random = Tetris.class.getDeclaredField("random");
        random.setAccessible(true);
        random.set(tetris, new Random());

        logicTimer = Tetris.class.getDeclaredField("logicTimer");
        logicTimer.setAccessible(true);
        logicTimer.set(tetris, new Clock(1.0f));

        nextType = Tetris.class.getDeclaredField("nextType");
        nextType.setAccessible(true);
        nextType.set(tetris, TileType.TypeI);

        // Inject missing methods
        spawnPiece = Tetris.class.getDeclaredMethod("spawnPiece");
        spawnPiece.setAccessible(true);

        resetGame = Tetris.class.getDeclaredMethod("resetGame");
        resetGame.setAccessible(true);

        updateGame = Tetris.class.getDeclaredMethod("updateGame");
        updateGame.setAccessible(true);

        rotatePiece = Tetris.class.getDeclaredMethod("rotatePiece", int.class);
        rotatePiece.setAccessible(true);

        // Default stubbing so spawn / update never trips gameOver
        when(mockBoard.isValidAndEmpty(any(), anyInt(), anyInt(), anyInt()))
                .thenReturn(true);
        when(mockBoard.checkLines()).thenReturn(0);
    }

    @Test
    public void testResetGameInitializesState() throws InvocationTargetException, IllegalAccessException {
        resetGame.invoke(tetris);

        assertFalse(tetris.isNewGame());
        assertFalse(tetris.isGameOver());
        assertEquals(1, tetris.getLevel());
        assertEquals(0, tetris.getScore());
    }

    @Test
    public void testSpawnPiecePositionsCorrectly() throws IllegalAccessException, InvocationTargetException {
        nextType.set(tetris, TileType.TypeI);
        spawnPiece.invoke(tetris);

        assertEquals(TileType.TypeI, tetris.getPieceType());
        assertEquals(TileType.TypeI.getSpawnColumn(), tetris.getPieceCol());
        assertEquals(TileType.TypeI.getSpawnRow(), tetris.getPieceRow());
    }

    @Test
    public void testRotatePieceWorks() throws IllegalAccessException, InvocationTargetException {
        nextType.set(tetris, TileType.TypeI);
        spawnPiece.invoke(tetris);
        rotatePiece.invoke(tetris, 1);

        assertEquals(TileType.TypeI, tetris.getPieceType());
        assertEquals(1, tetris.getPieceRotation());
        assertEquals(3, tetris.getPieceCol());
        assertEquals(1, tetris.getPieceRow());
    }

    @Test
    public void testUpdateGameMovesDownWhenValid() throws IllegalAccessException, InvocationTargetException {
        nextType.set(tetris, TileType.TypeT);
        spawnPiece.invoke(tetris);

        int before = tetris.getPieceRow();

        updateGame.invoke(tetris);

        assertEquals(before + 1, tetris.getPieceRow());
    }

    @Test
    public void testUpdateGameLandsAndScores() throws InvocationTargetException, IllegalAccessException {
        // Arrange gravity to fail so it lands
        when(mockBoard.isValidAndEmpty(any(), anyInt(), anyInt(), anyInt()))
                .thenReturn(true) // spawn ok
                .thenReturn(false); // then land
        when(mockBoard.checkLines()).thenReturn(2);

        resetGame.invoke(tetris);
        updateGame.invoke(tetris);

        verify(mockBoard).addPiece(any(), anyInt(), anyInt(), anyInt());
        assertEquals(200, tetris.getScore());  // 50 << 2
    }

    @Test
    public void testIsPausedReturnsCorrectValue() {
        boolean result = tetris.isPaused();

        assertFalse(result);
    }

    @Test
    public void testGetNextPieceTypeReturnsCorrectValue() throws IllegalAccessException {
        nextType.set(tetris, TileType.TypeS);
        TileType result = tetris.getNextPieceType();

        assertEquals(TileType.TypeS, result);
    }
}
