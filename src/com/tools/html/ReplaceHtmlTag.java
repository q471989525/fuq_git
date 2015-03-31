package com.tools.html;

/**
 * 去除Html标签
 * @author ljg
 */
public class ReplaceHtmlTag {

    private static final String[] regexs = {"<style.*?>.+?</style>",
        "<script.*?>.+?</script>",
        "<.+?>", "\\&.+?;",
        "^[　\\s]+", "[　\\s]+$"
    };

    public static String replace(String input) {
        input = replace(input, regexs);
        //去除中间多余的空格
        input = input.replaceAll("[　 ]{2,}", " ");
        return input;
    }

    /**
     * 去除HTML中的标签
     * @param input 输入
     * @param reg 要去除的正则表达式数组
     * @return
     */
    public static String replace(String input, String[] reg) {
        int length = reg.length;
        for (int i = 0; i < length; i++) {
            input = input.replaceAll(reg[i], "");
        }
        return input;
    }
}
