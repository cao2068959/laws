package znet.dataInterface.core;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {
	private static String defaultDatePattern = "yyyy-MM-dd HH:mm:ss";

	public static boolean isValidDate(String str) {
		boolean convertSuccess = true;
		// ָ�����ڸ�ʽΪ��λ��/��λ�·�/��λ���ڣ�ע��yyyy/MM/dd���ִ�Сд��
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			// ����lenientΪfalse.
			// ����SimpleDateFormat��ȽϿ��ɵ���֤���ڣ�����2007/02/29�ᱻ���ܣ���ת����2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			// e.printStackTrace();
			// ���throw java.text.ParseException����NullPointerException����˵����ʽ����
			convertSuccess = false;
		}
		return convertSuccess;
	}

	/**
	 * ���Ĭ�ϵ� date pattern
	 */
	public static String getDatePattern() {
		return defaultDatePattern;
	}

	/**
	 * ����Ԥ��Format�ĵ�ǰ�����ַ���
	 */
	public static String getToday() {
		Date today = new Date();
		return format(today);
	}

	/**
	 * ʹ��Ԥ��Format��ʽ��Date���ַ���
	 */
	public static String format(Date date) {
		return date == null ? " " : format(date, getDatePattern());
	}

	/**
	 * ʹ�ò���Format��ʽ��Date���ַ���
	 */
	public static String format(Date date, String pattern) {
		return date == null ? " " : new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * ʹ��Ԥ���ʽ���ַ���תΪDate
	 */
	public static Date parse(String strDate) {
		return parse(strDate, getDatePattern());
	}

	public static Date parseStr(String strDate) {
		if (isNumeric(strDate)) {
			SimpleDateFormat format = new SimpleDateFormat(defaultDatePattern);
			String time = format.format(strDate);
			return parse(time, getDatePattern());
		}
		return parse(strDate, getDatePattern());
	}

	/**
	 * ʹ�ò���Format���ַ���תΪDate
	 */
	public static Date parse(String strDate, String pattern) {
		Date date = null;
		try {
			date = new SimpleDateFormat(pattern).parse(strDate);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return date;
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * ��������������������
	 */
	public static Date addMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		return cal.getTime();
	}

	public static String getLastDayOfMonth(String year, String month) {
		Calendar cal = Calendar.getInstance();
		// ��
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		// �£���ΪCalendar������Ǵ�0��ʼ������Ҫ-1
		// cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		// �գ���Ϊһ��
		cal.set(Calendar.DATE, 1);
		// �·ݼ�һ���õ��¸��µ�һ��
		cal.add(Calendar.MONTH, 1);
		// ��һ���¼�һΪ�������һ��
		cal.add(Calendar.DATE, -1);
		return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));// �����ĩ�Ǽ���
	}

	public static Date getDate(String year, String month, String day)
			throws ParseException {
		String result = year + "- "
				+ (month.length() == 1 ? ("0 " + month) : month) + "- "
				+ (day.length() == 1 ? ("0 " + day) : day);
		return parse(result);
	}

	/**
	 * ������ת��Ϊ��ʱ����
	 * 
	 * @return
	 */
	public static String dateToTime(long times) {
		long second = times;
		second = second / 1000;
		long days = second / 86400;
		second = second % 86400;
		long hours = second / 3600;
		second = second % 3600;
		long minutes = second / 60;
		second = second % 60;
		if (days > 0) {
			return days + "��" + hours + "Сʱ" + minutes + "��" + second + "��";
		} else if (hours > 0) {
			return hours + "Сʱ" + minutes + "��" + second + "��";
		} else if (minutes > 0) {
			return minutes + "��" + second + "��";
		} else {
			return second + "��";
		}
	}



	public static Date parse(Timestamp time) {
		if (null != time) {
			SimpleDateFormat format = new SimpleDateFormat(defaultDatePattern);
			String d = format.format(time);
			return parse(d);
		}
		return null;
	}
	
	
	
	
	
}
