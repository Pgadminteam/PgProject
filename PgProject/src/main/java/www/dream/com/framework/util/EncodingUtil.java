package www.dream.com.framework.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/*
 * Utility class for JavaScript compatible UTF-8 encoding and decoding.
 * 파일을 첨부할 때 컴퓨터의 폴더에 저장을 해서 거기에서 받아와서 사용하는데 이름이 같은 파일이 여러 개 있을 경우 
 * 자동으로 불러올 때 다른 파일을 불러올 수 있어 그것을 방지하고자 폴더에 저장할때 파일의 이름을 인코딩을 해서 올려 그 인코딩된 이름을 불러와서 사용한다.
 * 폴더에 저장되는 파일은 인코딩이름_원래이름으로 저장함
 */

public class EncodingUtil {
	/**
	 * Encodes the passed String as UTF-8 using an algorithm that's compatible with
	 * JavaScript's <code>encodeURIComponent</code> function. Returns
	 * <code>null</code> if the String is <code>null</code>.
	 * 
	 * @param s The String to be encoded
	 * @return the encoded String
	 */
	public static String encodeURIComponent(String s) {
		String result = null;
		try {
			result = URLEncoder.encode(s, "UTF-8")
					.replaceAll("\\+", "%20").replaceAll("\\%21", "!")
					.replaceAll("\\%27", "'").replaceAll("\\%28", "(")
					.replaceAll("\\%29", ")").replaceAll("\\%7E", "~");
		}
		// This exception should never occur.
		catch (UnsupportedEncodingException e) {
			result = s;
		}
		return result;
	}
	/**
	 * Private constructor to prevent this class from being instantiated.
	 */
	private EncodingUtil() {
		super();
	}
}