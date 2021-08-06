package lexprd006.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Demo {

	public static void main(String args[]) {

		LocalDate lc = LocalDate.now();
		System.out.println(lc);

		String adate = "2021-03-20";
		String aRopnDate = "2021-03-24";
		String status = "Re_Opened";

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();

		try {

			String sysDate = dateFormat.format(date);
			Date auditDate = dateFormat.parse(adate);
			Date curDate = dateFormat.parse(sysDate);
			
			Date RopnDate = dateFormat.parse(aRopnDate);

			System.out.println("curDate : " + curDate + "\t auditDate : " + auditDate);

			if (status.equalsIgnoreCase("Re_Opened")) {
				if (curDate.before(auditDate)) {
					System.out.println("true : " + true);
				} else {
					System.out.println("false : " + false);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
