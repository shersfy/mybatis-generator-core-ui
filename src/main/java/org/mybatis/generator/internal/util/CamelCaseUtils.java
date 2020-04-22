package org.mybatis.generator.internal.util;

/**
 * 驼峰命名与下划线转换工具类
 * @author pengy
 * @date 2018年12月2日
 */
public final class CamelCaseUtils {

	private CamelCaseUtils() {}

	private static final String SEPARATOR = "_";
	
	/**
	 * 驼峰转为指定分隔符字符串
	 * @param separator
	 * @param camel
	 * @return
	 */
	public static String toSeparatorString(String camel, String separator) {
		if (separator==null || camel == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for (int i = 0; i < camel.length(); i++) {
			char c = camel.charAt(i);

			boolean nextUpperCase = true;

			if (i < (camel.length() - 1)) {
				nextUpperCase = Character.isUpperCase(camel.charAt(i + 1));
			}

			if ((i >= 0) && Character.isUpperCase(c)) {
				if (!upperCase || !nextUpperCase) {
					if (i > 0) sb.append(separator);
				}
				upperCase = true;
			} else {
				upperCase = false;
			}

			sb.append(Character.toLowerCase(c));
		}

		return sb.toString();
	}

	/**
	 * 驼峰转下划线
	 * @param camel
	 * @return
	 */
	public static String toUnderlineString(String camel) {
		return toSeparatorString(camel, SEPARATOR);
	}

	/**
	 * 转换为驼峰命名
	 * @param inputString
	 * @param firstCharacterUppercase
	 * @return
	 */
	public static String toCamelCaseString(String inputString,
            boolean firstCharacterUppercase) {
        StringBuilder sb = new StringBuilder();

        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);

            switch (c) {
            case '_':
            case '-':
            case '@':
            case '$':
            case '#':
            case ' ':
            case '/':
            case '&':
                if (sb.length() > 0) {
                    nextUpperCase = true;
                }
                break;

            default:
                if (nextUpperCase) {
                    sb.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
                break;
            }
        }

        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        return sb.toString();
    }

}
