/*
MariaDB Client for Java

Copyright (c) 2012 Monty Program Ab.

This library is free software; you can redistribute it and/or modify it under
the terms of the GNU Lesser General Public License as published by the Free
Software Foundation; either version 2.1 of the License, or (at your option)
any later version.

This library is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
for more details.

You should have received a copy of the GNU Lesser General Public License along
with this library; if not, write to Monty Program Ab info@montyprogram.com.

This particular MariaDB Client for Java file is work
derived from a Drizzle-JDBC. Drizzle-JDBC file which is covered by subject to
the following copyright and notice provisions:

Copyright (c) 2009-2011, Marcus Eriksson

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:
Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of the driver nor the names of its contributors may not be
used to endorse or promote products derived from this software without specific
prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS  AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
OF SUCH DAMAGE.
*/

package org.mariadb.jdbc.internal.mysql;

public class MySQLServerCapabilities {
    public static final int
            LONG_PASSWORD = 1,       /* new more secure passwords */
            FOUND_ROWS    = 2,       /* Found instead of affected rows */
            LONG_FLAG     = 4,       /* Get all column flags */
            CONNECT_WITH_DB = 8,     /* One can specify db on connect */
            NO_SCHEMA = 16,          /* Don't allow database.table.column */
            COMPRESS =  32,          /* Can use compression protocol */
            ODBC = 64,               /* Odbc client */
            LOCAL_FILES = 128,       /* Can use LOAD DATA LOCAL */
            IGNORE_SPACE = 256,       /* Ignore spaces before '(' */
            CLIENT_PROTOCOL_41 = 512, /* New 4.1 protocol */
            CLIENT_INTERACTIVE =  1024,
            SSL = 2048,                /* Switch to SSL after handshake */
            IGNORE_SIGPIPE = 4096,     /* IGNORE sigpipes */
            TRANSACTIONS = 8192,
            RESERVED = 16384,           /* Old flag for 4.1 protocol  */
            SECURE_CONNECTION = 32768,  /* New 4.1 authentication */
            MULTI_STATEMENTS = 1 << 16, /* Enable/disable multi-stmt support */
            MULTI_RESULTS = 1 << 17,    /* Enable/disable multi-results */
            PLUGIN_AUTH = 1 << 19,      /* Client supports plugin authentication */
            PROGRESS = 1 << 29;         /* Client support progress indicator */
}