package az.cybernet.invoice.enums;

import java.util.Map;
import java.util.Set;

public enum Status {
    DRAFT, // invoice is not yet finished
    SENT_TO_RECEIVER, // invoice is sent to receiver
    APPROVED, // invoice was approved by the user
    REJECTED, // invoice was rejected by the user without a request for changes
    CORRECTION_REQUESTED, // invoice is sent back for correction before approval
    CORRECTION_REQUESTED_AFTER_APPROVAL, // invoice is sent back for correction after approval
    SENT_TO_RECEIVER_AFTER_CORRECTION, // invoice is corrected and sent to receiver again
    CANCELLED_BY_SENDER, // invoice is cancelled after rejection or approval by sender
    CANCELLED_DUE_TO_TIMEOUT, // invoice is not approved by receiver before deadline


    //Previous statuses which will be deleted later
    CLOSED, //the invoice was fulfilled
    CHANGES_REQUESTED, //user requested changes
    DELETED, //invoice itself doesn't have is_active field so this may be used
    PENDING, //the invoice is awaiting approval/rejection/rfc
    CANCELLED;

    private static final Map<Status, Set<Status>> STATUS_TRANSITIONS = Map.of(
            DRAFT, Set.of(SENT_TO_RECEIVER, CANCELLED_BY_SENDER),
            SENT_TO_RECEIVER, Set.of(APPROVED, REJECTED, CORRECTION_REQUESTED, CANCELLED_DUE_TO_TIMEOUT),
            SENT_TO_RECEIVER_AFTER_CORRECTION, Set.of(APPROVED, REJECTED,
                    CORRECTION_REQUESTED, CANCELLED_DUE_TO_TIMEOUT),
            APPROVED, Set.of(CORRECTION_REQUESTED_AFTER_APPROVAL, CANCELLED_BY_SENDER),
            REJECTED, Set.of(CANCELLED_BY_SENDER),
            CORRECTION_REQUESTED, Set.of(SENT_TO_RECEIVER_AFTER_CORRECTION, CANCELLED_BY_SENDER),
            CORRECTION_REQUESTED_AFTER_APPROVAL, Set.of(SENT_TO_RECEIVER_AFTER_CORRECTION, CANCELLED_BY_SENDER)
    );


    public boolean canBeChangedTo(Status newStatus) {
        return STATUS_TRANSITIONS.getOrDefault(this, Set.of()).contains(newStatus);
    }
}

