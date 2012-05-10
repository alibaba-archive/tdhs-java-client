BestPratice
===========

* TDHSClient is thread-safe,so you must use it as SINGLETON object in your application.
* Statement which create by ``TDHSClient.createStatement()`` or ``TDHSClient.createBatchStatement()`` is **not** thread-safe
* You can set hashcode with ``TDHSClient.createStatement(hashcode)`` to assign the thread which server-side to run.