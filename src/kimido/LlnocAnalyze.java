package kimido;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * https://github.com/tmd45/lldecade
 * @author tmd45
 */
public class LlnocAnalyze {

	/**
	 * 解析結果の文章を返す。
	 * 引数が文字列の場合に利用する
	 *
	 * @param  llnocSecrecy
	 * @return 解析した文字列
	 */
	public String getAnalyzedSentence(String llnocSecrecy)
	{
		// TODO: あとで
		return null;
	}

	/**
	 * 解析結果の文章を返す。
	 * 引数がファイルの場合に利用する。
	 * 解析に失敗した場合は null を返す。
	 *
	 * @param llnocSecrecy
	 */
	public String getAnalyzedSentence(File llnocSecrecyFile)
	{
		if(llnocSecrecyFile != null)
		{
			FileInputStream   fs = null;
			InputStreamReader ir = null;
			BufferedReader    br = null;

			try
			{
				fs = new FileInputStream(llnocSecrecyFile);
				ir = new InputStreamReader(fs);
				br = new BufferedReader(ir);

				String line  = null;
				List<String> lines = new ArrayList<String>();

				while((line = br.readLine()) != null)
				{
					lines.add(line);
				}

				return analyze(lines);

			}
			catch (FileNotFoundException e)
			{
				return null;
			}
			catch (UnsupportedEncodingException e)
			{
				return null;
			}
			catch (IOException e)
			{
				return null;
			}
			finally
			{
				try
				{
					if(br != null) br.close();
					if(ir != null) ir.close();
					if(fs != null) fs.close();
				}
				catch (IOException e)
				{
					return null;
				}
			}

		}

		return null;
	}

	private String analyze(List<String> lines)
	{
		String sentense  = "";
		String binaryStr = "";

		int lineSize = lines.size();

		for(int i = 0; i < lineSize; i++)
		{
			String line = lines.get(i);
			binaryStr += analyzeLine(line);

			if((i+1)%4 == 0)
			{
				sentense += convAscii(binaryStr);
				binaryStr = "";
			}
			// 4つペアに出来なかったバイナリ値は無視
		}

		return sentense;
	}

	/**
	 * 一行ごとに解析を行う。
	 *
	 * @param  line
	 * @return 解析結果の文字
	 */
	private String analyzeLine(String line)
	{
		if(isMacAddr(line))   return "00";
		else if(isIpv4(line)) return "01";
		else if(isIpv6(line)) return "10";
		else                  return "11";
	}

	/**
	 * IPv4 か？
	 *
	 * @param str
	 * @return
	 */
	private boolean isIpv4(String str)
	{
		String[] splited = str.split("\\.");
		if(splited.length != 4) return false;

		for(int i = 0; i < 4; i++)
		{
			try
			{
				String numStr = splited[i];
				if(numStr.charAt(0) == '0') return false;
				Short num = Short.valueOf(numStr);
				if(num < 0 || num > 255) return false;
			}
			catch(Exception e)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * IPv6 か？
	 *
	 * @param str
	 * @return
	 */
	private boolean isIpv6(String str)
	{
		String[] splited = str.split(":");
		if(splited.length != 8) return false;

		for(int i = 0; i < 8; i++)
		{
			try
			{
				String numStr = splited[i];
				if(numStr.length() > 4) return false;
				if(numStr.length() > 2 && numStr.charAt(0) == '0') return false;
				Integer.parseInt(numStr, 16);	// 16進数チェック
			}
			catch(Exception e)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Mac アドレスか？
	 *
	 * @param str
	 * @return
	 */
	private boolean isMacAddr(String str)
	{
		int colon = str.indexOf(":");
		int dot   = str.indexOf("-");
		if(colon > 0 && dot > 0) return false;

		String[] splited = null;
		if   (colon > 0) splited = str.split(":");
		else if(dot > 0) splited = str.split("-");
		else             return false;

		if(splited.length != 6) return false;

		for(int i = 0; i < 6; i++)
		{
			try
			{
				if(splited[i].length() != 2) return false;
				Integer.parseInt(splited[i], 16);	// 16進数チェック
			}
			catch(Exception e)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * 解析結果のバイナリ値（2進数 8桁）を文字列に変換
	 *
	 * @param  binaryStr 解析バイナリ値
	 * @return ASCII文字列
	 */
	private String convAscii(String binaryStr)
	{
		String ascii = "";

		try
		{
			int dec = Integer.parseInt(binaryStr, 2);
			ascii = Character.toString((char)dec).toUpperCase();
//			System.out.println("convAscii : " + binaryStr);
//			System.out.println("convAscii : " + ascii);
		}
		catch(Exception e)
		{
			return "";
		}
		return ascii;
	}

}
