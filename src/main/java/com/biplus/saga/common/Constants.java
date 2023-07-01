package com.biplus.saga.common;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Constants {

    public static final String BEARER = "Bearer ";

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String CURRENCY = "TZS";
    public static final String PAY_METHOD = "1";
    public static final String CURRENT_ACTION_USER = "current.action.user";
    public static final String CUSTOMER = "customer";
    public static final String REPRESENTATIVE_CUSTOMER = "representative";
    public static final String BILLCYCLE = "BillCycleId";
    public static final String CONTACT_MEDIUM_TME = "TME";

    public static final String TOPUP = "TOPUP";
    public static final String ACTIVE_MOBILE_PREPAID = "ACTIVE_MOBILE_PREPAID";
    public static final String ACTIVE_MOBILE_POSTPAID = "ACTIVE_MOBILE_POSTPAID";
    public static final Long VAS_TYPE = 300L;
    public static final Set<Long> SERIAL_SIM_TYPE = Collections.unmodifiableSet(Set.of(4L, 8L));
    public static final Long ISDN_TYPE = 1L;
    public static final Long GOODS_SERIAL = 7L;
    public static final Long GOODS_NO_SERIAL = 11L;
    public static final Long GOODS_HANDSET = 10L;
    public static final Integer REPRESENT_ACTIVE = 1;
    public static final Integer REPRESENT_NOTACTIVE = 0;
    public static final Long TELECOM_SERVICE_ID_1 = 1L;
    public static final String EXPORTED = "3";
    public static final String IMPORTED = "1";

    public static final String CUSTOMER_PARTY = "customerParty";
    public static final String REPRSENTATIVE = "representative";



    private Constants() {

    }

    public static class EmailTemplate {
        public static final String RESPONSE_OFFER = "RESPONSE_OFFER";
        public static final String ONBOARD_STATUS = "ONBOARD_STATUS";
    }

    public static final class CHANGE_PRODUCT_TYPE{
        public static final String SALETRANS_COMMAND_PRODUCT_OFFER_CODE = "TOPUP";
    }

    public static final class AGREEMENT_ITEM_REPRESENTATIVE {
        public static final Integer REPRESENTATIVE_0 = 0;
        public static final Integer REPRESENTATIVE_1 = 1;
    }

    public static final class NimsAction {
        public static final Long LOCK_INFRASTRUCTURE = 1L;
        public static final Long UNLOCK_INFRASTRUCTURE = 0L;
    }

    public static final class TaskType {
        public static final Long OFFLINE_SURVEY = 1L; // khao sat
        public static final Long CONNECT_SUBSCRIBER = 1L;
        public static final Long CONNECT = 2L; // dau noi
        public static final Long DEPLOY = 3L; // trien khai
        public static final Long PROCESSING = 4L;
    }

    public static final class COUNTRY_CODE {
        public static final String LAO = "LA";
    }

    public static final class OrderStatus {
        public static final String BY_PASS_SURVEY = "544";
        public static final String ACTUAL_SURVEY = "543";
        public static final String DEFAULT_ORDER_SERVICE_STATUS = "6";
        public static final String BLOCK_IMEI_MOBILE_STATUS = "11";

        public static final Long TRANSFER_AGREEMENT = 48L;
        public static final String DONE = "6";
    }

    public static final class SagaName {


        public static final String CHANGE_PREPAID_TO_POSTPAID = "CHANGE_PREPAID_TO_POSTPAID";
        public static final String ACCEPT_CHANGE_DEPLOY_PROV_ADDRESS = "ACCEPT_CHANGE_DEPLOY_PROV_ADDRESS";
        public static final String CONNECT_CHANGE_DEPLOY_ADDRESS = "CONNECT_CHANGE_DEPLOY_ADDRESS";
        public static final String CHANGE_DEPLOY_ADDRESS = "CHANGE_DEPLOY_ADDRESS";
        public static final String CONNECT_PROVI_CHANGE_ADDRESS = "CONNECT_PROVI_CHANGE_ADDRESS";
        public static final String FINISH_CHANGE_ADDRESS = "FINISH_CHANGE_ADDRESS";
        public static final String CREATE_CHANGE_EQUIPMENT = "CREATE-CHANGE-EQUIPMENT";
        public static final String CHANGE_EQUIPMENT_WITH_TASK = "CHANGE-EQUIPMENT-WITH-TASK";
        public static final String ACCEPT_CHANGE_EQUIPMENT = "ACCEPT-CHANGE-EQUIPMENT";
        public static final String TERMINATE_ORDER = "TERMINATE-ORDER";
        public static final String TRANSFER_AGREEMENT = "TRANSFER-AGREEMENT";
        public static final String SIGN_TRANSFER_AGREEMENT = "SIGN-TRANSFER-AGREEMENT";
        public static final String RECEIVE_STOP_PRODUCT_AGREEMENT = "RECEIVE-STOP-PRODUCT-AGREEMENT";
        public static final String SPLIT_TRANSFER_AGREEMENT = "SPLIT-TRANSFER-AGREEMENT";
        public static final String BLOCK_OPEN_PRODUCT = "BLOCK-OPEN-PRODUCT";
        public static final String STOP_PRODUCT_AGREEMENT = "STOP-PRODUCT-AGREEMENT";
        public static final String CHANGE_PRE_2_POST = "CHANGE-PRE-2-POST";
        public static final String CHANGE_POST_2_PRE = "CHANGE-POST-2-PRE";
        public static final String CHANGE_POSTPAID_TO_PREPAID = "CHANGE_POSTPAID_TO_PREPAID";
        public static final String CHANGE_FTTH_TO_PREPAID = "CHANGE_FTTH_TO_PREPAID";


        public static final String UPSERT_PROSAL_ADJUST_ACCOUNT = "UPSERT-PROSAL-ADJUST-ACCOUNT";
        public static final String CONFIRM_PROSAL_ADJUST_ACCOUNT = "CONFIRM-PROSAL-ADJUST-ACCOUNT";
        public static final String CONFIRM_APPOINTMENT_REQUEST = "CONFIRM-APPOINTMENT-REQUEST";
        public static final String SIGN_TRANSFER_UPDATE_AGREEMENT = "SIGN-TRANSFER-UPDATE-AGREEMENT";
        public static final String SIGN_TRANSFER_RESTORE_PRODUCT = "SIGN-TRANSFER-RESTORE-PRODUCT";
    }

    public static final class Progress {
        public static final Long FINISH = 5l;
    }

    public final class CommitmentType {

        public static final String SUB = "0";
    }

    public static final class Topic {
        public static final String CHANGE_EQUIPMENT_WITH_TASK = "change_equipment_with_task";
        public static final String EVENT_CREATE_CHANGE_EQUIPMENT = "event_create_change_equipment";
        public static final String EVENT_ACCEPT_CHANGE_EQUIPMENT = "event_accept_change_equipment";
        public static final String EVENT_SAGA_OPEN_FIXLINE_PRODUCT = "event_open_fixline_product";
    }

    public static final class SALE_TRANS_INFO {

        public static final class TRANS_TYPE {
            public static final Long DAU_NOI = 1L;
            public static final Long DICH_VU = 4L;
        }

        public static final String CONTACT_ISDN_MEDIUM_TYPE = "TME";
        public static final String EMAIL_MEDIUM_TYPE = "EML";

    }

    public static final class CREATE_TASK_COMMAND {
        public static final Long TASK_TYPE_ID_5 = 5L;

    }

    public final class ActionCode {
        public static final String CHANGE_PRODUCT_OFFER = "33";
        public static final String CONNECT_PREPAID_MOBILE = "00";
        public static final String BLOCK_FIX_LINE = "06";
        public static final String OPEN_FIX_LINE = "08";
        public static final String EXTEND_PRODUCT = "565";
        public static final String CREATE_CHANGE_DEPLOY_ADDRESS = "104";
        public static final String CHANGE_DEVICE_WITH_TASK = "712";
        public static final String ADD_DEVICE = "916";
        public static final String CHANGE_PREPAID = "537";

        public static final String UPDATE_AGREEMENT = "91";
        public static final String SPLIT_AGREEMENT = "93";
        public static final String TRANSFER_PRODUCT_MOBILE = "1015";
        public static final String UPDATE_INCORRECT_PROFILE = "1005";
        public static final String BLOCK_IMEI_MOBILE = "999";
        public static final String UNBLOCK_IMEI_MOBILE = "999";
        public static final String UPDATE_NOT_CONNECT = "1004";
    }

    public final class Subscriber {
        public static final String ACTIVE = "030";
    }

    public static final class VmartType {
        public static final Long CREATE_CHANGE_DEPLOY_ADDRESS = 29L;
        public static final Long CONNECT_CHANGE_DEPLOY_ADDRESS = 2L;
    }

    public static final class OptionSetValue {
        public static final String CONFIG_REASON_ID_OPEN_PRODUCT = "CONFIG_REASON_ID_OPEN_PRODUCT";
    }

    public static final class Message {
        private Message() {
        }

        public static final String ERROR_SYSTEM = "error.system";
    }

    public static class Party {
        public static final String ORGANIZATION_TYPE = "Organization";
        public static final String INDIVIDUAL_TYPE = "Individual";

        public static class Role {
            public static final String CUSTOMER = "customer";
            public static final String REPRESENTATIVE = "representative";
        }

        public static class Status {
            public static final Long STATUS__3 = -3L;
        }
    }


    public static final List<String> ADDRESS_TYPE = Collections.unmodifiableList(List.of("VERIFY_ADD", "BILL_ADD"));
    public static final List<String> MEDIUMS_TYPE = Collections.unmodifiableList(List.of("EML", "TME", "RCR"));
    public static final String MEDIUMS_TYPE_RCR = "RCR";

    public static final class Payment {
        public static final Integer IS_OPEN = 1;
    }

    public static class SystemType {
        public static final String BCCS3 = "BCCS3";
    }

    public static final String SALETRANS_SERVICE =  "[SALETRANS_SERVICE]";
    public static final String AGREEMENT_SERVICE = "[AGREEMENT-SERVICE]";
    public static final String ORDER_SERVICE = "[ORDER-SERVICE]";
    public static final String PARTY_SERVICE = "[PARTY-SERVICE]";
    public static final String PROFILE_SERVICE = "[PROFILE-SERVICE]";
    public static final String CUSTOMER_SERVICE = "[CUSTOMER-SERVICE]";
    public static final String NETWORK_PRODUCT_SERVICE = "[NETWORK-PRODUCT-SERVICE]";
    public static final String SALE_SERVICE = "[SALE-SERVICE]";
    public static final String EWALLET_SERVICE = "[EWALLET-SERVICE]";
    public static final String PAYMENT_SERVICE = "[PAYMENT-SERVICE]";
    public static final String PROVISIONING_SERVICE = "[PROVISIONING-SERVICE]";
    public static final String TASK_SERVICE = "[TASK-SERVICE]";
    public static final String STOCK_TRANS_SERVICE = "[STOCK-TRANS-SERVICE]";
    public static final String INVENTORY_SERVICE = "[INVENTORY-SERVICE]";
    public static final String NIMS_SERVICE = "[NIMS-SERVICE]";
    public static final String PROBLEM_SERVICE = "[PROBLEM-SERVICE]";
    public static final Long ORDER_STATUS = 11l;
    public static final String ROLE_CUSTOMER = "customer";

    public static class NotifyStatus {
        public static final int UNREAD = 0;
        public static final int READ = 1;
    }

    public static class SagaOfflineStatus {
        public static final int INITIAL = 0;
        public static final int PROCESSING = 3;
        public static final int SUCCESS = 1;
        public static final int FAILURE = 2;
    }

    public static class CONNECTION_MOBILE {
        public static final Map<Long, String> CONNECTION_TYPE = Collections.unmodifiableMap(Map.ofEntries(
                Map.entry(1L, "CONNECT_MOBILE_SINGLE_PREPAID"),
                Map.entry(2L, "CONNECT_MOBILE_SINGLE_POSTPAID"),
                Map.entry(3L, "CONNECT_MOBILE_SINGLE_REGISTER_PRODUCT"),
                Map.entry(4L, "CONNECT_MOBILE_MULTIPLE_PREPAID"),
                Map.entry(5L, "CONNECT_MOBILE_MULTIPLE_POSTPAID"),
                Map.entry(6L, "CONNECT_MOBILE_MULTIPLE_REGISTER_PRODUCT")
        ));

        public static final Long CONNECT_MOBILE_SINGLE_POSTPAID = 2L;
        public static final Long CONNECT_MOBILE_MULTIPLE_POSTPAID = 5L;
        public static final Long CONNECT_MOBILE_MULTIPLE_ROW_POSTPAID = 9L;

    }

    public static final class MEDIUM_TYPE {
        public static final String EML = "EML";
        public static final String TME = "TME";
    }

    public static final class RequestType {
        public static final String CONNECT_MOBILE_NETWORK_PRODUCT = "CONNECT_MOBILE_NETWORK_PRODUCT";
        public static final String SIGN_TRANSFER_MOBILE_NETWORK_PRODUCT = "SIGN_TRANSFER_MOBILE_NETWORK_PRODUCT";
        public static final String UPLOAD_PROFILE_MOBILE_AFTER_SALE_FOR_TRANSFER = "UPLOAD_PROFILE_MOBILE_AFTER_SALE_FOR_TRANSFER";
        public static final String CHANGE_POST_2_PRE = "CHANGE_POST_2_PRE";
        public static final String CHANGE_PRE_2_POST = "CHANGE_PRE_2_POST";
        public static final String CHANGE_POSTPAID_TO_PREPAID = "CHANGE_POSTPAID_TO_PREPAID";
        public static final String CHANGE_PREPAID_TO_POSTPAID = "CHANGE_PREPAID_TO_POSTPAID";
        public static final String UPLOAD_PROFILE_MOBILE_AFTER_SALE = "UPLOAD_PROFILE_MOBILE_AFTER_SALE";
        public static final String SPLIT_AGREEMENT = "SPLIT_AGREEMENT";
        public static final String CHANGE_SIM = "CHANGE_SIM";
    }

    public static final class REASON {
        public static final String THLY = "1";
    }

    public static final String BILL_ADD = "BILL_ADD";
    public static final String VERIFY_ADD = "VERIFY_ADD";
    public static final String PARTY_ADD = "PARTY_ADD";

    public static final List<String> EXPORT_ORDER_HEADER = Collections.unmodifiableList(List.of("ORDER CODE", "RECEIVED STOCK", "REASON", "RUC", "PROJECT CODE", "BTS CODE/ACCOUNT", "GOODS CODE", "GOODS STATUS", "QUANTITY", "NOTE", "RESULT", "ERROR", "SAGA ID"));

    public final static class PRODUCT_OFFER_TYPE {
        public final static Long MOBILE = 1L;
        public final static Long HP = 2L;
        public final static Long PSTN = 3L;
        public final static Long SIM = 4L;
        public final static Long SIM_POST_PAID = 5L;
        public final static Long CARD = 6L;
        public final static Long HANDSET = 7L;
        public final static Long KIT = 8L;
        public final static Long ACCESSORIES = 10L;
        public final static Long NO_SERIAL = 11L;
    }

    public final static class MERGE_AGREEMENT {
        public final static Long DONE_STATUS = 6L;
        public final static String REQUEST_TYPE = "MERGE_AGREEMENT";
    }

    public static final class SPLIT_ITEM {
        public static final Long ZERO = 0L;
        public static final Long ONE = 1L;
    }

    public final static class TERMINATE_AGREEMENT {
        public final static String REQUEST_TYPE = "TERMINATE_AGREEMENT";
    }

    public static final class GEN_ACTION_CODE {
        public static final String PREFIX_CODE_IMPORT = "PN";
        public static final String BUSINESS_CODE_IMPORT = "IMP_STOCK_SENIOR";
        public static final String STEP_CODE_IMPORT = "NOTE";
    }

    public static final class ORDER_ITEM {
        public static final class ORDER_STATUS {
            public static final String COMPLETE = "48";
            public static final String CONNECTED = "33";
        }
    }

    public static final class BUSINESS_CODE {
        public static final String EXP_STOCK_TO_AGENT = "EXP_STOCK_TO_AGENT";
        public static final String REVOKE_STOCK_AGENT = "REVOKE_STOCK_AGENT";
        public static final String EXP_STOCK_COLLABORATOR = "EXP_STOCK_COLLABORATOR";
        public static final String REVOKE_STOCK_COLLABORATOR = "REVOKE_STOCK_COLLABORATOR";
    }

    public static final class IDENTITY_TYPE {
        public static final String TIN = "TIN";
        public static final String NUMBER_1 = "1";
    }
}
