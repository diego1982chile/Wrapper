package cl.forevision.wrapper.model;

/**
 * Created by root on 09-12-22.
 */
public enum ParameterEnum {

    USER_NAME("username"),
    PASSWORD("password"),
    BASE_URL_TOKEN("base_url_token"),
    BASE_URL_CONFIG("base_url_config"),
    TOKEN("token"),
    ERROR_TO("error.to"),
    FILE_DOWNLOAD_PATH("file.download_path"),
    MAIL_FROM_PASSWORD("mail.from.password"),
    MAIL_FROM_USER("mail.from.user"),
    MAIL_TO("mail.to"),
    SCRAPPER_PATH("scrapper.path");

    private String parameter;

    public String getParameter() {
        return this.parameter;
    }

    ParameterEnum(String parameter) {
        this.parameter = parameter;
    }
}
