package com.example.reportgeneratormk1.Interfaces;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Map;

public interface WordMaker {
    //parse the text and send to this
    public ByteArrayInputStream saveFile(Map<String,Object> jsonObj);
}
