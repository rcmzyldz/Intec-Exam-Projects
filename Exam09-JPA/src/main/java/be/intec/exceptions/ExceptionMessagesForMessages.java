package be.intec.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessagesForMessages {

    MESSAGE_INFO_REQUIRED("Message info is required."),
    MESSAGE_ID_IS_REQUIRED("Message ID required!"),
    MESSAGE_NAME_IS_REQUIRED("Message name required!"),
    MESSAGE_EMAIL_IS_REQUIRED("Message email required!"),
    MESSAGE_AGE_IS_REQUIRED("Message age required!"),
    SEARCH_KEYWORD_IS_REQUIRED("Search keyword is required!"),
    SEARCH_CRITERIA_IS_REQUIRED("Search criteria (minAge, maxAge) are required!"),

    MESSAGE_ID_IS_NOT_VALID("Message ID is NOT valid!"),
    MESSAGE_ID_MUST_NOT_BE_INITIALIZED("Message ID must NOT be initialized!"),
    MESSAGE_NAME_IS_NOT_VALID("Message name is NOT valid!"),
    MESSAGE_EMAIL_IS_NOT_VALID("Message email is NOT valid!"),
    MESSAGE_AGE_IS_NOT_VALID("Message age is NOT valid!"),
    SEARCH_KEYWORD_IS_NOT_VALID("Search keyword is NOT valid!"),
    SEARCH_CRITERIA_IS_NOT_VALID("Search criteria(s) is/are NOT valid!"),

    MESSAGE_NOT_FOUND("Message NOT found!"),
    MESSAGE_ALREADY_EXISTS("Message with this email already exist!"),
    UNDEFINED_EXCEPTION("There has been an undefined issue while processing information! Please try again.");

    private final String body;
}
