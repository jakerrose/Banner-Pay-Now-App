package com.doctums.banner.paymentgateway;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
//@ConditionalOnBean(JdbcTemplate.class)
//@ConditionalOnProperty(name = "student.attribute-source", havingValue = "banner")
public class StudentService {

    private final JdbcTemplate jdbcTemplate;
    private final BannerProperties bannerProperties;

    public StudentService(JdbcTemplate jdbcTemplate, BannerProperties bannerProperties) {
        this.jdbcTemplate = jdbcTemplate;
        this.bannerProperties = bannerProperties;
    }

    public StudentNameInfo getStudentInfoById(String studentId) {
        String sql = "SELECT spriden_first_name, spriden_last_name FROM spriden WHERE spriden_id = ? AND spriden_change_ind IS NULL";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, studentId);

        if (rows.isEmpty()) {
            return null;
        }

        Map<String, Object> row = rows.get(0);
        String firstName = (String) row.get("SPRIDEN_FIRST_NAME");
        String lastName = (String) row.get("SPRIDEN_LAST_NAME");
        System.out.println("spriden_first_name: " + row.get("SPRIDEN_FIRST_NAME"));
        System.out.println("spriden_last_name: " + row.get("SPRIDEN_LAST_NAME"));

        return new StudentNameInfo(firstName, lastName);
    }

    public StudentEmailInfo getEmailById(String studentId) {
        String emailType = bannerProperties.getEmailType();
        String emailSql = "SELECT g.goremal_email_address FROM spriden s LEFT JOIN goremal g ON g.goremal_pidm = s.spriden_pidm WHERE s.spriden_id = ? AND s.spriden_change_ind IS NULL AND g.goremal_emal_code = ? AND g.goremal_status_ind = 'A'";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(emailSql, studentId, emailType);

        if (rows.isEmpty()) {
            return null;
        }
        Map<String, Object> row = rows.get(0);
        String email = (String) row.get("GOREMAL_EMAIL_ADDRESS");
        System.out.println("email_address: " + row.get("GOREMAL_EMAIL_ADDRESS"));
        return new StudentEmailInfo(email);
    }

    public StudentPhoneInfo getPhoneById(String studentId) {
        String phoneSql = "SELECT a.sprtele_phone_area, a.sprtele_phone_number FROM spriden s JOIN sprtele a ON a.sprtele_pidm = s.spriden_pidm WHERE s.spriden_id = ? AND s.spriden_change_ind IS NULL";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(phoneSql, studentId);

        if (rows.isEmpty()) {
            return null;
        }
        Map<String, Object> row = rows.get(0);
        String areaCode = (String) row.get("sprtele_phone_area");
        String phoneNumber = (String) row.get("sprtele_phone_number");
        System.out.println("phone_area: " + row.get("sprtele_phone_area"));
        System.out.println("phone_number: " + row.get("sprtele_phone_number"));
        return new StudentPhoneInfo(areaCode, phoneNumber);
    }

    public StudentAddressInfo getAddressInfoById(String studentId) {
        String addressType = bannerProperties.getAddressType();
        String addressSql = "SELECT a.spraddr_street_line1, a.spraddr_street_line2, a.spraddr_city, a.spraddr_stat_code, a.spraddr_zip, a.spraddr_natn_code FROM spriden s JOIN spraddr a ON a.spraddr_pidm = s.spriden_pidm WHERE s.spriden_id = ? " +
                "AND a.spraddr_atyp_code = ? AND s.spriden_change_ind IS NULL";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(addressSql, studentId, addressType);

        if (rows.isEmpty()) {
            return null;
        }
        Map<String, Object> row = rows.get(0);
        String address1 = (String) row.get("SPRADDR_STREET_LINE1");
        String address2 = (String) row.get("SPRADDR_STREET_LINE2");
        String city = (String) row.get("spraddr_city");
        String state = (String) row.get("spraddr_stat_code");
        String zip = (String) row.get("spraddr_zip");
        String nationCode = (String) row.get("spraddr_natn_code");
        System.out.println("address1: " + row.get("spraddr_street_line1"));
        System.out.println("address2: " + row.get("spraddr_street_line2"));
        System.out.println("city: " + row.get("spraddr_city"));
        System.out.println("state: " + row.get("spraddr_stat_code"));
        System.out.println("zip: " + row.get("spraddr_zip"));
        System.out.println("nation_code: " + row.get("spraddr_natn_code"));
        return new StudentAddressInfo(address1, address2, city, state, zip, nationCode);
    }

    public StudentBalanceInfo getBalanceById(String studentId) {
        //String balanceSql = "SELECT SUM(d.tbraccd_balance) AS total_outstanding_balance FROM tbraccd d JOIN spriden s ON s.spriden_pidm = d.tbraccd_pidm WHERE s.spriden_id = ? AND s.spriden_change_ind IS NULL";
        String balanceSql = "SELECT s.spriden_id               AS student_id,\n" +
                "       MAX(d.tbraccd_term_code)   AS most_recent_term,\n" +
                "       SUM(d.tbraccd_balance)     AS total_outstanding_balance\n" +
                "FROM   tbraccd d\n" +
                "JOIN   spriden s\n" +
                "       ON s.spriden_pidm = d.tbraccd_pidm\n" +
                "WHERE  s.spriden_change_ind IS NULL\n" +
                "  AND  s.spriden_id = ?" +
                "GROUP BY s.spriden_id";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(balanceSql, studentId);

        if (rows.isEmpty()) {
            return null;
        }
        Map<String, Object> row = rows.get(0);
        BigDecimal accountBalance = (BigDecimal) row.get("total_outstanding_balance");
        String termCode = (String) row.get("most_recent_term");
        System.out.println("account_balance: " + row.get("total_outstanding_balance"));
        System.out.println("term_code: " + row.get("most_recent_term"));
        return new StudentBalanceInfo(accountBalance, termCode);
    }

    public StudentNationInfo getNationName(String nationCode) {
        String nationSql = "SELECT a.stvnatn_scod_code_iso FROM spraddr s JOIN stvnatn a ON a.stvnatn_code= s.spraddr_natn_code WHERE s.spraddr_natn_code = ? AND s.spraddr_status_ind IS NULL FETCH FIRST 1 ROWS ONLY";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(nationSql, nationCode);

        if (rows.isEmpty()) {
            return null;
        }
        Map<String, Object> row = rows.get(0);
        String nationName = (String) row.get("STVNATN_SCOD_CODE_ISO");
        System.out.println("nation_name: " + row.get("STVNATN_SCOD_CODE_ISO"));
        return new StudentNationInfo(nationName);
    }
}
