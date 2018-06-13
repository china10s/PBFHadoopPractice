package com.hadoop.hive;

import jodd.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.common.classification.RetrySemantics;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.hive.ql.exec.UDF;

public class strip extends UDF{
    private Text result = new Text();

    public Text evaluate(Text str){
        if (str == null){
            return null;
        }
        result.set(StringUtils.strip(str.toString()));
        return result;
    }

    public Text evaluate(Text str, String stripChars) {
        if (str == null){
            return null;
        }
        result.set(StringUtils.strip(str.toString(),stripChars));
        return result;
    }
}
