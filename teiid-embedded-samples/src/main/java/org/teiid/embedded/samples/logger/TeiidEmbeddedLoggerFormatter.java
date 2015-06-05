package org.teiid.embedded.samples.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

@SuppressWarnings("nls")
public class TeiidEmbeddedLoggerFormatter extends Formatter {
    
    SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy HH:mm");

    @Override
    public String format(LogRecord record) {
        StringBuffer sb = new StringBuffer();
        sb.append(format.format(new Date(record.getMillis())) + " ");
        sb.append(record.getLevel() + " ");
        sb.append("[" + record.getLoggerName() + "] ");
        sb.append(record.getMessage() + "\n");
        return sb.toString();
    }

}
