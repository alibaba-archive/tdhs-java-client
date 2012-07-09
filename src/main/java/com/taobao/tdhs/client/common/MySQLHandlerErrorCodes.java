package com.taobao.tdhs.client.common;

/**
 * @author <a href="mailto:wentong@taobao.com">文通</a>
 * @since 12-7-9 上午11:36
 */
public enum MySQLHandlerErrorCodes implements IMySQLHandlerErrorCodes {

    HA_ERR_KEY_NOT_FOUND(120, "Didn't find key on read or update "),
    HA_ERR_FOUND_DUPP_KEY(121, "Dupplicate key on write"),
    HA_ERR_INTERNAL_ERROR(122, "Internal error"),
    HA_ERR_RECORD_CHANGED(123, " Uppdate with is recoverable "),
    HA_ERR_WRONG_INDEX(124, " Wrong index given to function "),
    HA_ERR_CRASHED(126, " Indexfile is crashed "),
    HA_ERR_WRONG_IN_RECORD(127, " Record-file is crashed "),
    HA_ERR_OUT_OF_MEM(128, " Record-file is crashed "),
    HA_ERR_NOT_A_TABLE(130, " not a MYI file - no signature "),
    HA_ERR_WRONG_COMMAND(131, " Command not supported "),
    HA_ERR_OLD_FILE(132, " old databasfile "),
    HA_ERR_NO_ACTIVE_RECORD(133, " No record read in update() "),
    HA_ERR_RECORD_DELETED(134, " A record is not there "),
    HA_ERR_RECORD_FILE_FULL(135, " No more room in file "),
    HA_ERR_INDEX_FILE_FULL(136, " No more room in file "),
    HA_ERR_END_OF_FILE(137, " end in next/prev/first/last "),
    HA_ERR_UNSUPPORTED(138, " unsupported extension used "),
    HA_ERR_TO_BIG_ROW(139, " Too big row "),
    HA_WRONG_CREATE_OPTION(140, " Wrong create option "),
    HA_ERR_FOUND_DUPP_UNIQUE(141, " Dupplicate unique on write "),
    HA_ERR_UNKNOWN_CHARSET(142, " Can't open charset "),
    HA_ERR_WRONG_MRG_TABLE_DEF(143, " conflicting tables in MERGE "),
    HA_ERR_CRASHED_ON_REPAIR(144, " Last (automatic?) repair failed "),
    HA_ERR_CRASHED_ON_USAGE(145, " Table must be repaired "),
    HA_ERR_LOCK_WAIT_TIMEOUT(146, " lock wait time out"),
    HA_ERR_LOCK_TABLE_FULL(147, "lock table full"),
    HA_ERR_READ_ONLY_TRANSACTION(148, " Updates not allowed "),
    HA_ERR_LOCK_DEADLOCK(149, "dead lock"),
    HA_ERR_CANNOT_ADD_FOREIGN(150, " Cannot add a foreign key constr. "),
    HA_ERR_NO_REFERENCED_ROW(151, " Cannot add a child row "),
    HA_ERR_ROW_IS_REFERENCED(152, " Cannot delete a parent row "),
    HA_ERR_NO_SAVEPOINT(153, " No savepoint with that name "),
    HA_ERR_NON_UNIQUE_BLOCK_SIZE(154, " Non unique key block size "),
    HA_ERR_NO_SUCH_TABLE(155, " The table does not exist in engine "),
    HA_ERR_TABLE_EXIST(156, " The table existed in storage engine "),
    HA_ERR_NO_CONNECTION(157, " Could not connect to storage engine "),
    HA_ERR_NULL_IN_SPATIAL(158, " NULLs are not supported in spatial index "),
    HA_ERR_TABLE_DEF_CHANGED(159, " The table changed in storage engine "),
    HA_ERR_NO_PARTITION_FOUND(160, " There's no partition in table for given value "),
    HA_ERR_RBR_LOGGING_FAILED(161, " Row-based binlogging of row failed "),
    HA_ERR_DROP_INDEX_FK(162, " Index needed in foreign key constr "),
    HA_ERR_FOREIGN_DUPLICATE_KEY(163,
            "Upholding foreign key constraints would lead to a duplicate key error  in some other table."),
    HA_ERR_TABLE_NEEDS_UPGRADE(164, " The table changed in storage engine "),
    HA_ERR_TABLE_READONLY(165, " The table is not writable "),
    HA_ERR_AUTOINC_READ_FAILED(166, " Failed to get next autoinc value "),
    HA_ERR_AUTOINC_ERANGE(167, " Failed to set row autoinc value "),
    HA_ERR_GENERIC(168, " Generic error "),
    HA_ERR_RECORD_IS_THE_SAME(169, " row not actually updated: new values same as the old values "),
    HA_ERR_LOGGING_IMPOSSIBLE(170, " It is not possible to log this statement "),
    HA_ERR_CORRUPT_EVENT(171, " The event was corrupt, leading to illegal data being read "),
    HA_ERR_NEW_FILE(172, " New file format "),
    HA_ERR_ROWS_EVENT_APPLY(173, " The event could not be processed no other hanlder error happened "),
    HA_ERR_INITIALIZATION(174, " Error during initialization "),
    HA_ERR_FILE_TOO_SHORT(175, " File too short "),
    HA_ERR_WRONG_CRC(176, " Wrong CRC on page "),
    HA_ERR_TOO_MANY_CONCURRENT_TRXS(177, "Too many active concurrent transactions "),
    HA_ERR_INDEX_COL_TOO_LONG(178, " Index column length exceeds limit "),
    HA_ERR_INDEX_CORRUPT(179, " Index corrupted "),
    HA_ERR_UNDO_REC_TOO_BIG(180, " Undo log record too big ");
    private int code;

    private String desc;

    private static MySQLHandlerErrorCodes[] cached_code = new MySQLHandlerErrorCodes[200];

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * Constructor MySQLHandlerErrorCodes creates a new MySQLHandlerErrorCodes instance.
     *
     * @param code of type int
     * @param desc of type String
     */
    MySQLHandlerErrorCodes(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static IMySQLHandlerErrorCodes valueOf(final int code) {
        if (code < 0 || code >= cached_code.length) {
            return new DefaultMySQLHandlerErrorCodes("HA_UNKNOWN", code, "unknown code");
        }
        if (cached_code[code] != null) {
            return cached_code[code];
        }
        for (MySQLHandlerErrorCodes t : MySQLHandlerErrorCodes.values()) {
            cached_code[code] = t;
            if (code == t.getCode()) {
                return t;
            }
        }
        return new DefaultMySQLHandlerErrorCodes("HA_UNKNOWN", code, "unknown code");

    }

    @Override
    public String toString() {
        return "MySQLHandlerErrorCodes{" +
                "name=" + this.name() +
                ", code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }

    public static class DefaultMySQLHandlerErrorCodes implements IMySQLHandlerErrorCodes {
        private final String name;
        private final int code;
        private final String desc;

        public DefaultMySQLHandlerErrorCodes(String name, int code, String desc) {
            this.name = name;
            this.code = code;
            this.desc = desc;
        }

        public String name() {
            return name;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        @Override
        public String toString() {
            return "DefaultMySQLHandlerErrorCodes{" +
                    "name='" + name + '\'' +
                    ", code=" + code +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }
}
