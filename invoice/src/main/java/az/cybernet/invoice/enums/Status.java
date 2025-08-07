package az.cybernet.invoice.enums;

public enum Status {
    APPROVED, //user approved the invoice
    REJECTED, //user rejected the invoice without a request for changes
    DRAFT, //invoice is not yet finished
    CLOSED, //invoice was fulfilled
    CHANGES_REQUESTED, //user requested changes
    DELETED, //invoice itself doesn't have is_active field so this may be used
    CANCELLED, //invoice that expired more than a month ago
    PENDING; //invoice is awaiting approval/rejection/rfc

    public boolean canBeChangedTo(Status newStatus) {
        return switch (this) {
            case DRAFT -> newStatus == PENDING || newStatus == CLOSED;
            case PENDING -> newStatus == APPROVED || newStatus == REJECTED || newStatus == CHANGES_REQUESTED;
            case CHANGES_REQUESTED -> newStatus == PENDING;
            default -> false;
        };
    }
}
