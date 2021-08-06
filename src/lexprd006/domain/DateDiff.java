package lexprd006.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class DateDiff {

	public static void main(String args[]) throws ParseException {
		// Create SimpleDateFormat object
		SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");

		Date curDate = new Date();
		String format3 = sdfo.format(curDate);
		Date currentDate = sdfo.parse(format3);
		Date d2 = sdfo.parse("2021-04-10");

		String cuDate = sdfo.format(currentDate);
		String format2 = sdfo.format(d2);
		LocalDate chDate = LocalDate.parse(cuDate);
		LocalDate auditDate = LocalDate.parse(format2);
		System.out.println("currentDate : " + chDate + "\t AuditDate : " + auditDate);

		if (chDate.isAfter(auditDate) || auditDate.equals(chDate)) {
			System.out.println(
					"date2.isBefore(chDate) : " + auditDate.isBefore(chDate) + "\t OR " + auditDate.equals(chDate));
			System.out.println("Yes");
		} else {
			System.out.println("else : " + chDate.isAfter(auditDate));
			System.out.println("No");
		}

	}
}
