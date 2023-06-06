package com.trganda.ruby;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Ruby {
    public static void main(String[] args) throws ScriptException {
        String jrubyCode =
                "require \"java\"\njava.lang.Runtime.getRuntime().exec(\"open -a Calculator.app\")";
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("jruby");
        engine.eval(jrubyCode);
    }
}
