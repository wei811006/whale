package help.wei.whale.domain.employee;

public enum DayOffType {

    ANNUAL_LEAVE("特休"),
    REQUIRED_LEAVE("必休"),
    ROTATING_LEAVE("輪休"),
    ;

    private final String chineseName;

    DayOffType(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return chineseName;
    }

}
