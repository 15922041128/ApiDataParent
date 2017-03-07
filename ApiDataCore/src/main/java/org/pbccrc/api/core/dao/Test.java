package org.pbccrc.api.core.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Test {

	public static void main(String[] args) {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:Oracle:thin:@192.168.62.47:1521:orcl";
		String userName = "root";
		String password = "root";
		String innerId = "1";
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		String sql = " select * from employment where innerid = ?";
		
		url = "jdbc:oracle:thin:@localhost:1521:orcl";
		userName = "xcs10001";
		password = "xcsxcs";
		innerId = "15138167902";

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userName, password);
			ps = con.prepareStatement(sql);
			ps.setString(1, innerId);
			rs = ps.executeQuery();
			while (rs.next()) {
				String financeCode = rs.getString("financeCode");
				String occupation = rs.getString("occupation");
				String caste = rs.getString("caste");
				System.out.println("occupation: " + occupation + " ,caste: " + caste + " ,financeCode: " + financeCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
