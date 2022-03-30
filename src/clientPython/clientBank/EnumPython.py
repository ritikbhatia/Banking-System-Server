import enum


class Currency(enum.Enum):
    SGD = 1
    INR = 2
    USD = 3


class Status(enum.Enum):
    FAILURE = 0
    SUCCESS = 1
    ERROR = 2


class OpType(enum.Enum):
    CREATE_ACCOUNT = 0
    CLOSE_ACCOUNT = 1
    DEPOSIT_MONEY = 2
    WITHDRAW_MONEY = 3
    TRANSFER_MONEY = 4
    TRANSACTION_HISTORY = 5
    MONITOR_UPDATES = 6
