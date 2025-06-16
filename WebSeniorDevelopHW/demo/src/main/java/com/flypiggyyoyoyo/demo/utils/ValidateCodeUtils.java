package com.flypiggyyoyoyo.demo.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ValidateCodeUtils {

    // 验证码字符集
    private static final String CODE_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // 默认宽度
    private static final int DEFAULT_WIDTH = 100;
    // 默认高度
    private static final int DEFAULT_HEIGHT = 40;
    // 默认验证码长度
    private static final int DEFAULT_CODE_LENGTH = 4;
    // 干扰线数量
    private static final int INTERFERENCE_LINES = 20;
    // 干扰点数量
    private static final int INTERFERENCE_POINTS = 100;

    /**
     * 生成验证码图片并返回验证码文本
     * @param width 图片宽度
     * @param height 图片高度
     * @param codeLength 验证码长度
     * @param outputStream 输出流
     * @return 生成的验证码文本
     * @throws IOException 输出异常
     */
    public static String generateCodeImage(int width, int height, int codeLength, OutputStream outputStream) throws IOException {
        // 创建图像缓冲区
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 设置背景色
        g.setColor(getRandomColor(200, 250));
        g.fillRect(0, 0, width, height);

        // 绘制干扰线
        drawInterferenceLines(g, width, height);

        // 绘制干扰点
        drawInterferencePoints(g, width, height);

        // 生成随机验证码
        String code = generateRandomCode(codeLength);

        // 绘制验证码
        drawCode(g, code, width, height);

        // 释放资源
        g.dispose();

        // 输出图像到流
        ImageIO.write(image, "JPEG", outputStream);

        return code;
    }

    /**
     * 生成随机颜色
     */
    private static Color getRandomColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 绘制干扰线
     */
    private static void drawInterferenceLines(Graphics2D g, int width, int height) {
        Random random = new Random();
        g.setColor(getRandomColor(160, 200));

        for (int i = 0; i < INTERFERENCE_LINES; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 绘制干扰点
     */
    private static void drawInterferencePoints(Graphics2D g, int width, int height) {
        Random random = new Random();
        g.setColor(getRandomColor(120, 240));

        for (int i = 0; i < INTERFERENCE_POINTS; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g.drawOval(x, y, 1, 1);
        }
    }

    /**
     * 生成随机验证码
     */
    private static String generateRandomCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CODE_CHARS.length());
            code.append(CODE_CHARS.charAt(index));
        }

        return code.toString();
    }

    /**
     * 绘制验证码
     */
    private static void drawCode(Graphics2D g, String code, int width, int height) {
        Random random = new Random();
        g.setFont(new Font("Arial", Font.BOLD, 24));

        for (int i = 0; i < code.length(); i++) {
            g.setColor(new Color(
                    random.nextInt(128),
                    random.nextInt(128),
                    random.nextInt(128)
            ));

            // 随机旋转角度
            double angle = random.nextDouble() * 0.4 - 0.2; // -0.2 ~ 0.2弧度
            g.rotate(angle, width * (i + 0.5) / code.length(), height / 2);

            // 绘制字符
            g.drawString(
                    String.valueOf(code.charAt(i)),
                    (int) (width * (i + 0.2) / code.length()),
                    (int) (height * 0.7)
            );

            // 恢复旋转
            g.rotate(-angle, width * (i + 0.5) / code.length(), height / 2);
        }
    }

    /**
     * 生成验证码并返回包含验证码和图片Base64的Map
     */
    public static Map<String, String> generateCodeMap() {
        Map<String, String> result = new HashMap<>();
        try {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            String code = generateCodeImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_CODE_LENGTH, baos);

            // 将图片转换为Base64编码
            String base64Image = java.util.Base64.getEncoder().encodeToString(baos.toByteArray());

            result.put("code", code);
            result.put("image", "data:image/jpeg;base64," + base64Image);

            return result;
        } catch (IOException e) {
            throw new RuntimeException("生成验证码图片失败", e);
        }
    }

    /**
     * 校验验证码（忽略大小写）
     */
    public static boolean verifyCode(String generatedCode, String userInput) {
        return generatedCode != null && generatedCode.equalsIgnoreCase(userInput);
    }
}