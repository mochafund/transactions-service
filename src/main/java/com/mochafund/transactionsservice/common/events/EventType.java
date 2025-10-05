package com.mochafund.transactionsservice.common.events;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EventType {
    public static final String WORKSPACE_CREATED = "workspace.created";
    public static final String WORKSPACE_DELETED = "workspace.deleted";

    public static final String TAG_CREATED = "tag.created";
    public static final String TAG_DELETED = "tag.deleted";

    public static final String ACCOUNT_CREATED = "account.created";
    public static final String ACCOUNT_DELETED = "account.deleted";

    public static final String CATEGORY_CREATED = "category.created";
    public static final String CATEGORY_DELETED = "category.deleted";

    public static final String TRANSACTION_CREATED = "transaction.created";
    public static final String TRANSACTION_UPDATED = "transaction.updated";
    public static final String TRANSACTION_DELETED = "transaction.deleted";

    public static final String MERCHANT_CREATED = "merchant.created";
    public static final String MERCHANT_DELETED = "merchant.deleted";
}
