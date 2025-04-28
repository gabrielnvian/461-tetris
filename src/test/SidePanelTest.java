package test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.psnbtech.BoardPanel;
import org.psnbtech.SidePanel;
import org.psnbtech.Tetris;
import org.psnbtech.TileType;

import java.awt.image.BufferedImage;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SidePanelTest {
    // Create a Mockito mock for the Tetris dependency
    @Mock
    private Tetris fakeGame;

    // Have Mockito call the SidePanel(Tetris) constructor for us
    @InjectMocks
    private SidePanel panel;

    @Before
    public void setUp() {
        // Stub out any methods paintComponent() or our logic tests will hit
        when(fakeGame.getLevel()).thenReturn(5);
        when(fakeGame.getScore()).thenReturn(50);
    }

    @Test
    public void testPaintComponentDoesNotThrowWithGameOver() {
        when(fakeGame.isGameOver()).thenReturn(true);

        // Create an off-screen image, so the panel has *something* to draw onto
        BufferedImage img = new BufferedImage(
                BoardPanel.PANEL_WIDTH, BoardPanel.PANEL_HEIGHT,
                BufferedImage.TYPE_INT_ARGB
        );
        panel.paintComponent(img.getGraphics());
        // no exception = success
    }

    @Test
    public void testPaintComponentDoesNotThrowWithNextPiece() {
        when(fakeGame.isGameOver()).thenReturn(false);
        when(fakeGame.getNextPieceType()).thenReturn(TileType.TypeO);

        // Create an off-screen image, so the panel has *something* to draw onto
        BufferedImage img = new BufferedImage(
                BoardPanel.PANEL_WIDTH, BoardPanel.PANEL_HEIGHT,
                BufferedImage.TYPE_INT_ARGB
        );
        panel.paintComponent(img.getGraphics());
        // no exception = success
    }

    @Test
    public void testPaintComponentDoesNotThrowWithoutNextPiece() {
        when(fakeGame.isGameOver()).thenReturn(false);
        when(fakeGame.getNextPieceType()).thenReturn(null);

        // Create an off-screen image, so the panel has *something* to draw onto
        BufferedImage img = new BufferedImage(
                BoardPanel.PANEL_WIDTH, BoardPanel.PANEL_HEIGHT,
                BufferedImage.TYPE_INT_ARGB
        );
        panel.paintComponent(img.getGraphics());
        // no exception = success
    }
}