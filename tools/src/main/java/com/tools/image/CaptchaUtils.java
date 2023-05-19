package com.tools.image;

import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtils {

    protected static final Random random = new Random(System.nanoTime());

    // 默认的验证码大小
    protected static final int WIDTH = 108, HEIGHT = 40;
    // 验证码随机字符数组
    protected static final char[] charArray = "3456789ABCDEFGHJKMNPQRSTUVWXY".toCharArray();
    // 验证码字体
    protected static final Font[] RANDOM_FONT = new Font[] {
        new Font(Font.DIALOG, Font.BOLD, 33),
        new Font(Font.DIALOG_INPUT, Font.BOLD, 34),
        new Font(Font.SERIF, Font.BOLD, 33),
        new Font(Font.SANS_SERIF, Font.BOLD, 34),
        new Font(Font.MONOSPACED, Font.BOLD, 34)
    };

    public static String getImageCode() {
        char[] randomChars = new char[4];
        for (int i=0; i<randomChars.length; i++) {
            randomChars[i] = charArray[random.nextInt(charArray.length)];
        }
        return String.valueOf(randomChars);
    }


    public static void drawGraphic(String randomString, BufferedImage image){
        // 获取图形上下文
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        // 图形抗锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 字体抗锯齿
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 设定背景色
        g.setColor(getRandColor(210, 250));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //绘制小字符背景
        Color color = null;
        for(int i = 0; i < 20; i++){
            color = getRandColor(120, 200);
            g.setColor(color);
            String rand = String.valueOf(charArray[random.nextInt(charArray.length)]);
            g.drawString(rand, random.nextInt(WIDTH), random.nextInt(HEIGHT));
            color = null;
        }

        //设定字体
        g.setFont(RANDOM_FONT[random.nextInt(RANDOM_FONT.length)]);
        // 绘制验证码
        for (int i = 0; i < randomString.length(); i++){
            //旋转度数 最好小于45度
            int degree = random.nextInt(28);
            if (i % 2 == 0) {
                degree = degree * (-1);
            }
            //定义坐标
            int x = 22 * i, y = 21;
            //旋转区域
            g.rotate(Math.toRadians(degree), x, y);
            //设定字体颜色
            color = getRandColor(20, 130);
            g.setColor(color);
            //将认证码显示到图象中
            g.drawString(String.valueOf(randomString.charAt(i)), x + 8, y + 10);
            //旋转之后，必须旋转回来
            g.rotate(-Math.toRadians(degree), x, y);
        }
        //图片中间曲线，使用上面缓存的color
        g.setColor(color);
        //width是线宽,float型
        BasicStroke bs = new BasicStroke(3);
        g.setStroke(bs);
        //画出曲线
        QuadCurve2D.Double curve = new QuadCurve2D.Double(0d, random.nextInt(HEIGHT - 8) + 4, WIDTH / 2, HEIGHT / 2, WIDTH, random.nextInt(HEIGHT - 8) + 4);
        g.draw(curve);
        // 销毁图像
        g.dispose();
    }

    /*
     * 给定范围获得随机颜色
     */
    protected static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}
