package az.cybernet.invoice.enums;

public enum Status {
    APPROVED, //the invoice was approved by the user
    REJECTED, //the invoice was rejected by the user without a request for changes
    DRAFT, //the invoice is not yet finished
    CLOSED, //the invoice was fulfilled
    CHANGES_REQUESTED, //user requested changes
    DELETED, //invoice itself doesn't have is_active field so this may be used
    PENDING;//the invoice is awaiting approval/rejection/rfc

    public boolean canBeChangedTo(Status newStatus) {
        switch (this) {
            case DRAFT:
                return newStatus == PENDING || newStatus == CLOSED;
            case PENDING:
                return newStatus == APPROVED || newStatus == REJECTED || newStatus == CHANGES_REQUESTED;
            case CHANGES_REQUESTED:
                return newStatus == PENDING;
            default:
                return false;
        }
    }
}
