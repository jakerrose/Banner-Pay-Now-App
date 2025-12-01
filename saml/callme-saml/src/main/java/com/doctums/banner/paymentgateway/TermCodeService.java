package com.doctums.banner.paymentgateway;

import org.springframework.stereotype.Service;

@Service
public class TermCodeService {
    private final BannerProperties bannerProperties;

    public TermCodeService(BannerProperties bannerProperties) {
        this.bannerProperties = bannerProperties;
    }

    public String translateTermCode(String termCode) {
        if (termCode == null || termCode.length() != 6) {
            return termCode; // fallback
        }

        String year = termCode.substring(0, 4);
        String code = termCode.substring(4, 6);

        if ("semesters".equalsIgnoreCase(bannerProperties.getAcademicSystem())) {
            switch (code) {
                case "10": return year + "/SP";
                case "20": return year + "/SU";
                case "30": return year + "/FA";
                default:   return termCode;
            }
        } else if ("quarters".equalsIgnoreCase(bannerProperties.getAcademicSystem())) {
            switch (code) {
                case "10": return year + "/WI";
                case "20": return year + "/SP";
                case "30": return year + "/SU";
                case "40": return year + "/FA";
                default:   return termCode;
            }
        }

        return termCode;
    }
}
