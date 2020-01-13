package promo_creation.objects;


import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;

public class DirectPromo {

    String id;
    String name;
    String isActive;
    String description;
    String voucherBatchId;

    String promoCodePrefix;
    String lengthOfPromoCode;
    String numberOfPromoCode;
    String start;
    String end;
    String validityType;
    String validityPeriod;

    String enTitle;
    String enMsg;
    String viTitle;
    String viMsg;

    String deepLink;

    String includeSegments;
    String includeOperator;
    String excludeSegments;
    String excludeOperator;

    String includeS2Ids;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getIsActive() {
        return isActive;
    }
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getVoucherBatchId() {
        return voucherBatchId;
    }
    public void setVoucherBatchId(String voucherBatchId) {
        this.voucherBatchId = voucherBatchId;
    }

    public String getStart() {
        return start;
    }
    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }
    public void setEnd(String end) {
        this.end = end;
    }

    public String getValidityType() {
        return validityType;
    }
    public void setValidityType(String validityType) {
        this.validityType = validityType;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }
    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getPromoCodePrefix() {
        return promoCodePrefix;
    }
    public void setPromoCodePrefix(String promoCodePrefix) {
        this.promoCodePrefix = promoCodePrefix;
    }

    public String getLengthOfPromoCode() {
        return lengthOfPromoCode;
    }
    public void setLengthOfPromoCode(String lengthOfPromoCode) {
        this.lengthOfPromoCode = lengthOfPromoCode;
    }

    public String getNumberOfPromoCode() {
        return numberOfPromoCode;
    }
    public void setNumberOfPromoCode(String numberOfPromoCode) {
        this.numberOfPromoCode = numberOfPromoCode;
    }

    public String getEnTitle() {
        return enTitle;
    }
    public void setEnTitle(String enTitle) {
        this.enTitle = enTitle;
    }

    public String getEnMsg() {
        return enMsg;
    }
    public void setEnMsg(String enMsg) {
        this.enMsg = enMsg;
    }

    public String getViTitle() { return viTitle; }
    public void setViTitle(String viTitle) { this.viTitle = viTitle; }

    public String getViMsg() { return viMsg; }
    public void setViMsg(String viMsg) { this.viMsg = viMsg; }

    public String getDeepLink() { return deepLink; }
    public void setDeepLink(String deepLink) { this.deepLink = deepLink; }

    public String getIncludeSegments() {
        return includeSegments;
    }
    public void setIncludeSegments(String includeSegments) {
        this.includeSegments = includeSegments;
    }

    public String getIncludeOperator() { return includeOperator; }
    public void setIncludeOperator(String includeOperator) { this.includeOperator = includeOperator; }

    public String getExcludeSegments() {
        return excludeSegments;
    }
    public void setExcludeSegments(String excludeSegments) {
        this.excludeSegments = excludeSegments;
    }

    public String getExcludeOperator() { return excludeOperator; }
    public void setExcludeOperator(String excludeOperator) { this.excludeOperator = excludeOperator; }

    public String toString() {
        return "Direct promo [name=" + name + ", isActive=" + isActive + ", description=" + description +
                ", voucherBatchId=" + voucherBatchId + ", start=" + start + ", end=" + end + ", validityType=" + validityType +
                ", validityPeriod=" + validityPeriod +
                ", promoCodePrefix=" + promoCodePrefix + ", lengthOfPromoCode=" + lengthOfPromoCode +
                ", numberOfPromoCode=" + numberOfPromoCode +", enTitle=" + enTitle +
                ", enMsg=" + enMsg +", viTitle=" + viTitle +
                ", viMsg=" + viMsg +", deepLink=" + deepLink +
                ", includeSegments=" + includeSegments +", includeOperator=" + includeOperator +
                ", excludeSegments=" + excludeSegments +", excludeOperator=" + excludeOperator +
                ", includeS2Ids=" + includeS2Ids + ", id=" + id + "]";
    }

    public DirectPromo(String csvLine) {
        String[] splitter = StringUtils.split(csvLine, ",");
        setters(splitter);
    }

    private String prepVoucherBatchId(String voucherBatchId) {
        String test;
        try {
            test = voucherBatchId.replaceAll("&", ",");
        } catch (Exception e) {
            test = voucherBatchId;
        }
        return test;
    }

    private void setters(String[] directPromoArray) {
        System.out.println("setters BEGIN");
        this.name = directPromoArray[0].trim();
        this.isActive = directPromoArray[1].trim();
        this.description = directPromoArray[2].trim();
        this.voucherBatchId = prepVoucherBatchId(directPromoArray[3].trim());
        this.promoCodePrefix = directPromoArray[4].trim();
        this.lengthOfPromoCode = directPromoArray[5].trim();
        this.numberOfPromoCode = directPromoArray[6].trim();
        this.start = directPromoArray[7].trim();
        this.end = directPromoArray[8].trim();
        this.validityType = directPromoArray[9].trim();
        this.validityPeriod = directPromoArray[10].trim();
        this.enTitle = directPromoArray[11].trim();
        this.enMsg = directPromoArray[12].trim();
        this.viTitle = directPromoArray[13].trim();
        this.viMsg = directPromoArray[14].trim();
        this.deepLink = directPromoArray[15].trim();
        /*this.includeS2Ids = directPromoArray[16].trim();*/
        /*this.includeSegments = directPromoArray[17].trim();*/
        this.includeOperator = directPromoArray[16].trim();
        /*this.excludeSegments = directPromoArray[19].trim();*/
        this.excludeOperator = directPromoArray[17].trim();
        if (this.id == null) {
            this.id = "";
        }
        System.out.println("setters END");
    }


    public DirectPromo(String name, String isActive, String description, String voucherBatchId, String promoCodePrefix,
                       String lengthOfPromoCode, String numberOfPromoCode, String start, String end, String validityType, String validityPeriod,
                       String enTitle, String enMsg, String viTitle, String viMsg, String deepLink,
                       String includeSegments, String includeOperator, String excludeSegments, String excludeOperator, String includeS2Ids, String id) throws ParseException {

        this.name = name;
        this.isActive = isActive;
        this.description = description;
        this.voucherBatchId = voucherBatchId;

        this.promoCodePrefix = promoCodePrefix;
        this.lengthOfPromoCode = lengthOfPromoCode;
        this.numberOfPromoCode = numberOfPromoCode;

        this.start = start;
        this.end = end;
        this.validityType = validityType;

        this.validityPeriod = validityPeriod;

        this.enTitle = enTitle;
        this.enMsg = enMsg;
        this.viTitle = viTitle;
        this.viMsg = viMsg;

        this.deepLink = deepLink;

        this.includeS2Ids = includeS2Ids;

        this.includeSegments = includeSegments;
        this.includeOperator = includeOperator;
        this.excludeSegments = excludeSegments;
        this.excludeOperator = excludeOperator;

        this.id = id;
    }

    public String[] toArray() {
        return new String[]{name, isActive, description, voucherBatchId, promoCodePrefix, lengthOfPromoCode, numberOfPromoCode,
                start, end, validityType, validityPeriod, enTitle, enMsg, viTitle, viMsg, deepLink, includeS2Ids, includeSegments, includeOperator, excludeSegments, excludeOperator, id};
    }
}
